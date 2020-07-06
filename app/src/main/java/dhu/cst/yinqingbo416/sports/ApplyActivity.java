package dhu.cst.yinqingbo416.sports;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dhu.cst.yinqingbo416.sports.Entry.ActivityInfo;
import dhu.cst.yinqingbo416.sports.Entry.SUser;
import dhu.cst.yinqingbo416.sports.CustomControl.mAlertDialog;
import dhu.cst.yinqingbo416.sports.Utils.DBUtils;
import dhu.cst.yinqingbo416.sports.Utils.Tools;

public class ApplyActivity extends AppCompatActivity {
    private TextView actTitle;//活动主题
    private TextView actTime;//活动时间
    private TextView actSite;//活动地点
    private TextView actAward;//活动奖励
    private TextView applyTime;//报名截止时间
    private TextView actNumber1;//正式队员名额
    private TextView actNumber2;//替补队员名额
    private TextView actDept;//活动主办单位
    private TextView actDetail;//活动详情
    private TextView actRemark;//备注信息
    private ActivityInfo act;//活动信息
    private int position;//活动列表的位置
    private Button applyBtn;//报名按钮
    private List<Integer>applyActId;//储存已报名的活动编号
    private boolean isApply = false;//标志用户是否报名了该活动
    private boolean isOption = false;//标记用户报名状态是否更改
    private LinearLayout remarkContain;
    private SUser stuUser = new SUser();
    private String TAG = "ApplyActivity";
    private Handler handler = null;
    private int kind;//活动类型
    private mAlertDialog progressDialog;//圆形加载进度条
    //标记要更新的名额数据类型 1更新number 2更新man_number 3更新woman_number 4更新bench_number 5更新bench_man_number 6更新bench_woman_number
    private int numberSign = 0;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        handler = new Handler(){//接收消息
            public void handleMessage(Message msg) {
                if(msg.what == 1){//读取活动名额数据成功
                    detailActNumber();//修改活动名额数据
                    progressDialog.cancel();
                    detailApply();
                }else if(msg.what == 2){//读取活动名额数据失败
                    Toast.makeText(ApplyActivity.this,"请求发送失败，请检查网络后重试！",Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }else if(msg.what == 3){//活动报名成功
                    mAlertDialog dialog1;
                    dialog1 = new mAlertDialog(ApplyActivity.this,"成功","报名成功！");
                    dialog1.show();
                    if(!applyActId.contains(act.getId())){
                        applyActId.add(act.getId());
                        Tools.setApplyActId(applyActId);
                    }
                    isApply = true;
                    applyBtn.setText("取消报名");
                    progressDialog.cancel();
                    updateActNumber();
                }else if(msg.what == 4){//活动报名失败
                    mAlertDialog dialog1;
                    dialog1 = new mAlertDialog(ApplyActivity.this,"失败","报名失败，请稍后重试！");
                    dialog1.show();
                    progressDialog.cancel();
                }else if(msg.what == 5){//取消报名成功
                    //更新活动数据
                    updateAct();
                }else if(msg.what == 6){//取消报名失败
                    mAlertDialog dialog1;
                    dialog1 = new mAlertDialog(ApplyActivity.this,"失败","取消报名失败，请稍后重试！");
                    dialog1.show();
                    progressDialog.cancel();
                }else if(msg.what == 7){//取消报名成功且数据更新成功
                    progressDialog.cancel();
                    mAlertDialog dialog1;
                    dialog1 = new mAlertDialog(ApplyActivity.this,"成功","取消报名成功！");
                    dialog1.show();
                    for(int i = 0;i < applyActId.size();i++){
                        if(applyActId.get(i) == act.getId()){
                            applyActId.remove(i);
                            Tools.setApplyActId(applyActId);
                            break;
                        }
                    }
                    isApply = false;
                    applyBtn.setText("报名");
                    detailActNumber();
                }
            }
        };
        progressDialog = new mAlertDialog(ApplyActivity.this,"Loading");//圆形进度条对话框
        init();
    }

    //初始化函数
    public void init(){//初始化控件和内容
        act = (ActivityInfo) getIntent().getSerializableExtra("Act");
        position = (int) getIntent().getSerializableExtra("Position");
        applyActId = Tools.getApplyActId();
        kind = act.getKind();
        actTitle = findViewById(R.id.apply_act_title);
        actTime = findViewById(R.id.apply_act_time);
        actSite = findViewById(R.id.apply_act_site);
        actAward = findViewById(R.id.apply_act_award);
        applyTime = findViewById(R.id.apply_end_time);
        actNumber1 = findViewById(R.id.apply_act_number1);
        actNumber2 = findViewById(R.id.apply_act_number2);
        actDept = findViewById(R.id.apply_act_dept);
        actDetail = findViewById(R.id.apply_act_detail);
        actRemark = findViewById(R.id.apply_act_remark);
        applyBtn = findViewById(R.id.apply_button);
        remarkContain = findViewById(R.id.apply_act_remark_container);
        actTitle.setText(act.getName());
        actTime.setText(act.getTime());
        actSite.setText(act.getSite());
        actAward.setText(act.getAward());
        applyTime.setText(act.getEndTime());
        detailActNumber();//活动名额信息展示的处理
        detailActDepts();//对活动主办部门信息展示进行处理
        actDetail.setText(act.getDetails());
        if(!act.getRemark().equals("")){
            remarkContain.setVisibility(View.VISIBLE);
            actRemark.setText(act.getRemark());
        }
        //读取用户数据
        getUserInfo();
        if(applyActId.contains(act.getId())){//已报名
            isApply = true;
            isOption = true;
            applyBtn.setText("取消报名");
        }else {
            isApply = false;
            isOption = false;
            applyBtn.setText("报名");
        }
        //报名按钮
        applyBtn.setOnClickListener(new View.OnClickListener() {//报名按钮点击事件
            @Override
            public void onClick(View v) {
                if(!isApply){
                    applyAct();
                }else {
                    cancelAct();
                }
            }
        });
    }
    //对活动名额及进行处理
    public void detailActNumber(){
        String str = "首发：";
        String str_bench = "替补：";
        if(kind == 1 || kind == 3){
            str += act.getPeople()+" / "+act.getNumber();
        }else if(kind == 2 || kind == 4){
            str += "  男："+act.getMan_people()+" / "+act.getMan_number()+"  女："+act.getWoman_people()+" / "+act.getWoman_number();
        }else if(kind == 5){
            str += act.getPeople()+" / "+act.getNumber();
            str_bench += act.getBench_people() +" / "+ act.getBench_number();
        }else if(kind == 6){
            str += "  男："+act.getMan_people()+" / "+act.getMan_number()+"  女："+act.getWoman_people()+" / "+act.getWoman_number();
            str_bench += act.getBench_people()+" / "+act.getBench_number();
        }else if(kind == 7){
            str += "  男："+act.getMan_people()+" / "+act.getMan_number()+"  女："+act.getWoman_people()+" /" +act.getWoman_number();
            str_bench += "  男："+act.getBench_man_people()+" / "+act.getBench_man_number()+"  女："+act.getBench_woman_people()+" / "+act.getBench_woman_number();
        }
        if(kind >= 5){
            actNumber2.setVisibility(View.VISIBLE);
            actNumber2.setText(str_bench);
        }
        actNumber1.setText(str);
    }
    //对活动部门的展示进行处理
    public void detailActDepts(){
        String str = "";
        for(String item:act.getDept()){
            if(!str.equals("")){
                str += "\n";
            }
            str += item;
        }
        actDept.setText(str);
    }
    //读取用户的报名信息
    public void getUserInfo(){
        SharedPreferences pref = getSharedPreferences("stuData",MODE_PRIVATE);
        stuUser.setStudentId(pref.getString("id",""));
        stuUser.setCollegeId(pref.getString("collegeId",""));
        stuUser.setCollegeName(pref.getString("collegeName",""));
        stuUser.setName(pref.getString("name",""));
        stuUser.setSex(pref.getString("sex",""));
        stuUser.setPhone(pref.getString("phone",""));
    }
    //根据名额进行报名处理
    public void detailApply(){
        if(kind <= 4){//没有替补
            int people = 0;
            int number = 0;
            if(kind == 1 || kind == 3){//1、不分学院，不分性别，不分替补 3、分学院，不分性别，不分替补
                people = act.getPeople();
                number = act.getNumber();
                numberSign = 1;
            }else if(kind == 2 || kind == 4){//2、不分学院，分性别,不分替补 4、分学院，分性别，不分替补
                if(stuUser.getSex().equals("男")){
                    people = act.getMan_people();
                    number = act.getMan_number();
                    numberSign = 2;
                }else {
                    people = act.getWoman_people();
                    number = act.getWoman_number();
                    numberSign = 3;
                }
            }
            if(people > number){
                //报名活动
                AlertDialog.Builder dialog = new AlertDialog.Builder(ApplyActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("名额充足！确认报名活动:"+act.getName()+"吗？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertApply(true);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                //向数据库中插入信息同时更新报名统计表
            }else {
                mAlertDialog dialog = new mAlertDialog(ApplyActivity.this,"提示","名额已满！");
                dialog.show();
            }
        }else {//有替补
            int people = 0;
            int number = 0;
            int bench_people = 0;
            int bench_number = 0;
            if(kind == 5){//分学院，不分性别，分替补
                people = act.getPeople();
                number = act.getNumber();
                bench_people = act.getBench_people();
                bench_number = act.getBench_number();
                numberSign = 1;
            }else if(kind == 6){//分学院 分性别，分替补
                if(stuUser.getSex().equals("男")){
                    people = act.getMan_people();
                    number = act.getMan_number();
                    bench_people = act.getBench_people();
                    bench_number = act.getBench_number();
                    numberSign = 2;
                }else {
                    people = act.getWoman_people();
                    number = act.getWoman_number();
                    bench_people = act.getBench_people();
                    bench_number = act.getBench_number();
                    numberSign = 3;
                }
            }else if(kind == 7){//分学院 分性别 分替补 替补分性别
                if(stuUser.getSex().equals("男")){
                    people = act.getMan_people();
                    number = act.getMan_number();
                    bench_people = act.getBench_man_people();
                    bench_number = act.getBench_man_number();
                    numberSign = 2;
                }else {
                    people = act.getWoman_people();
                    number = act.getWoman_number();
                    bench_people = act.getBench_woman_people();
                    bench_number = act.getBench_woman_number();
                    numberSign = 3;
                }
            }
            if(people > number && bench_people > bench_number){
                AlertDialog.Builder dialog = new AlertDialog.Builder(ApplyActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("名额充足！请选择报名类型");
                dialog.setCancelable(true);
                dialog.setPositiveButton("首发", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertApply(true);
                    }
                });
                dialog.setNegativeButton("替补", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(kind == 7){
                            numberSign += 3;
                        }else {
                            numberSign = 4;
                        }
                        insertApply(false);
                    }
                });
                dialog.show();
            }else if(people > number){
                AlertDialog.Builder dialog = new AlertDialog.Builder(ApplyActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("替补名额不足！是否报名该活动的首发队员？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertApply(true);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }else if(bench_people > bench_number){
                AlertDialog.Builder dialog = new AlertDialog.Builder(ApplyActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("首发名额不足！是否报名该活动的替补队员？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertApply(false);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }else {
                mAlertDialog dialog = new mAlertDialog(ApplyActivity.this,"提示","名额已满,报名失败！");
                dialog.show();
            }
        }
    }
    //向数据库插入数据
    public void insertApply(final boolean kind){
        progressDialog.progressbar_show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ret = DBUtils.applyAct(act.getId(),kind,stuUser);
                Message msg = new Message();
                if(ret){
                    msg.what = 3;
                }else {
                    msg.what = 4;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }
    //报名成功后修改名额信息
    public void updateActNumber(){
        int k = -1;
        if(isApply){
            k = 1;
        }
        switch (numberSign){
            case 1:
                act.setNumber(act.getNumber()+k);
                break;
            case 2:
                act.setMan_number(act.getMan_number()+k);
                break;
            case 3:
                act.setWoman_number(act.getWoman_number()+k);
                break;
            case 4:
                act.setBench_number(act.getBench_number()+k);
                break;
            case 5:
                act.setBench_man_number(act.getBench_man_number()+k);
                break;
            case 6:
                act.setBench_woman_number(act.getWoman_number()+k);
                break;
            default:
                break;
        }
        detailActNumber();
    }
    //报名活动
    public void applyAct(){
        progressDialog.progressbar_show();
        //首先判断活动的名额是否满了
        new Thread(new Runnable() {
            @Override
            public void run() {
                ActivityInfo temp;
                Message msg = new Message();
                if(kind <= 2){
                    temp = DBUtils.updateActInfo(act,"g");
                }else {
                    temp = DBUtils.updateActInfo(act,stuUser.getCollegeId());
                }
                if(temp == null){
                    msg.what = 2;
                }else {
                    msg.what = 1;
                    act = temp;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }
    //取消报名
    public void cancelAct(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ApplyActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("确定要取消报名吗？");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.progressbar_show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean ret = DBUtils.cancelAct(act.getId(),stuUser.getStudentId());
                        Log.d(TAG, "ret:"+ret);
                        Message msg = new Message();
                        if(ret){
                            msg.what = 5;
                        }else {
                            msg.what = 6;
                        }
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });
        dialog.show();
    }
    //取消报名后更新活动信息
    private void updateAct() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                act = DBUtils.updateActInfo(act,stuUser.getCollegeId());
                Message msg = new Message();
                if(act == null){
                    Log.d(TAG, "act:null");
                    msg.what = 6;
                }else {
                    msg.what = 7;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }
    //重写返回键，返回报名后的更新信息
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("Act",act);
        intent.putExtra("Position",position);
        isOption = isOption != isApply;
        intent.putExtra("isOption",isOption);
        setResult(RESULT_OK,intent);
        finish();
    }
}
