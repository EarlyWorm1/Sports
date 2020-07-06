package dhu.cst.yinqingbo416.sports;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zyyoona7.wheel.WheelView;

import java.util.ArrayList;
import java.util.List;

import dhu.cst.yinqingbo416.sports.Utils.DBUtils;
import dhu.cst.yinqingbo416.sports.Utils.CheckInput;
import dhu.cst.yinqingbo416.sports.Entry.SUser;
import dhu.cst.yinqingbo416.sports.Utils.Tools;
import dhu.cst.yinqingbo416.sports.CustomControl.CustomTextInputLayout;
import dhu.cst.yinqingbo416.sports.CustomControl.mAlertDialog;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private EditText studentId;
    private EditText college;
    private EditText className;
    private EditText name;
    private RadioGroup sex;
    private EditText phone;
    private EditText email;
    private EditText passWord;
    private EditText surePassWord;
    private TextView btnSignIn;
    private String str_sex = "男";
    private String str_id;
    private String str_class;
    private String str_college;
    private String str_name;
    private String str_phone;
    private String str_email;
    private String str_passWord;
    private String str_sure;
    private CustomTextInputLayout textInputLayout_id;
    private CustomTextInputLayout textInputLayout_class;
    private CustomTextInputLayout textInputLayout_name;
    private CustomTextInputLayout textInputLayout_phone;
    private CustomTextInputLayout textInputLayout_email;
    private CustomTextInputLayout textInputLayout_passWord;
    private CustomTextInputLayout textInputLayout_surePassWord;
    private CustomTextInputLayout textInputLayout_college;
    private boolean []flag = {false,false,false,false,false,false,false};
    private boolean []sign = {false,false,false,false,false,false,false};
    private String errorMassage = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setStatusBarTransparent();//设置透明状态栏
        init();//初始化函数
        id_preprocessor();//对班级输入进行预处理
        college_preprocessor();//对学院输入进行处理
        class_preprocessor();//对班级输入进行预处理
        userName_proprecessor();//对姓名输入进行预处理
        phone_proprecessor();//对电话输入进行预处理
        email_proprecessor();//对邮件输入进行预处理
        passWord_proprecessor();//对密码输入进行预处理
        sure_passWord_proprecessor();//对确认密码输入进行预处理
    }
    //初始化函数
    public void init(){
        back = findViewById(R.id.sign_in_back);
        studentId = findViewById(R.id.sign_in_input_layout_id);
        textInputLayout_id = findViewById(R.id.TextInputLayout_id);
        college = findViewById(R.id.sign_in_input_layout_college);
        textInputLayout_college = findViewById(R.id.TextInputLayout_college);
        className = findViewById(R.id.sign_in_input_layout_class);
        textInputLayout_class = findViewById(R.id.TextInputLayout_class);
        name = findViewById(R.id.sign_in_input_layout_name);
        textInputLayout_name = findViewById(R.id.TextInputLayout_name);
        sex = findViewById(R.id.sign_in_input_layout_sex);
        phone = findViewById(R.id.sign_in_input_layout_phone);
        textInputLayout_phone = findViewById(R.id.TextInputLayout_phone);
        email = findViewById(R.id.sign_in_input_layout_email);
        textInputLayout_email = findViewById(R.id.TextInputLayout_email);
        passWord = findViewById(R.id.sign_in_input_layout_passWord);
        textInputLayout_passWord = findViewById(R.id.TextInputLayout_passWord);
        surePassWord = findViewById(R.id.sign_in_input_layout_sure_passWord);
        textInputLayout_surePassWord = findViewById(R.id.TextInputLayout_surePassWord);
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);
        back.setOnClickListener(this);
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton choise = findViewById(id);
                str_sex = choise.getText().toString();
            }
        });
    }
    //设置透明状态栏
    private void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_back:
                Intent intent = new Intent(SignInActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_sign_in:
                str_id = studentId.getText().toString();
                str_college = college.getText().toString();
                str_class = className.getText().toString();
                str_name = name.getText().toString();
                str_phone = phone.getText().toString();
                str_email = email.getText().toString();
                str_passWord = passWord.getText().toString();
                str_sure = surePassWord.getText().toString();
                errorMassage = "";
                boolean flog = true;
                for(int i = 0;i < 7;i++){
                    sign[i] = true;
                    if(!flag[i]){
                        flog = false;
                        showErrorMessage(i);
                    }
                }
                if(str_college.equals("")){
                    flog = false;
                    textInputLayout_college.setError("学院不能为空");
                    errorMassage += "、学院";
                }
                if(flog){
                    //检查密码和确认密码是否一致
                    if(str_passWord.equals(str_sure)){
                        //userSignIn();
                        checkUserInfo();
                    }else {
                        Toast.makeText(this,"密码和确认密码不一致",Toast.LENGTH_SHORT).show();
                        textInputLayout_surePassWord.setError("两次密码不一致");
                    }
                }else{
                    errorMassage += "信息缺失或错误";
                    Toast.makeText(this,errorMassage,Toast.LENGTH_SHORT).show();
                }
        }
    }
    //重写返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(SignInActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    //对学号输入进行预处理
    public void id_preprocessor(){
        //监听学号的输入
        studentId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ret = CheckInput.isStudentid(s.toString());
                flag[0] = ret.equals(" ");
                if(sign[0]){
                    textInputLayout_id.setError(ret);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        //监听学号焦点
        studentId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String s = studentId.getText().toString();
                    textInputLayout_id.setError(CheckInput.isStudentid(s));
                    sign[0] = true;
                }
            }
        });
    }
    public void college_preprocessor(){
        final List<String> list = new ArrayList<>();
        list.add("纺织学院");
        list.add("服装与艺术设计学院");
        list.add("旭日工商管理学院");
        list.add("机械工程学院");
        list.add("信息科学与技术学院");
        list.add("计算机科学与技术学院");
        list.add("化学化工与生物工程学院");
        list.add("材料科学与工程学院");
        list.add("环境科学与工程学院");
        list.add("人文学院");
        list.add("理学院");
        list.add("外语学院");
        list.add("马克思主义学院");
        list.add("上海国际时尚创意学院");
        list.add("国际文化交流学院");
        college.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.wheel_view,null);
                WheelView<String>wheelView = view.findViewById(R.id.wheelView);
                wheelView.setData(list);
                wheelView.setTextSize(70);
                wheelView.setVisibleItems(7);
                wheelView.setSelectedItemPosition(4);
                wheelView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(WheelView<String> wheelView, String data, int position) {
                        college.setText(data);
                        textInputLayout_college.setError(" ");
                    }
                });
                AlertDialog.Builder dialog = new AlertDialog.Builder(SignInActivity.this);
                dialog.setTitle("请选择学院");
                dialog.setView(view);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(college.getText().toString().equals("")){
                            college.setText("信息科学与技术学院");
                        }
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }
    //对班级输入进行预处理
    public void class_preprocessor(){
        //监听班级的输入
        className.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ret = CheckInput.isClassName(s.toString());
                flag[1] = ret.equals(" ");
                if(sign[1]){
                    textInputLayout_class.setError(ret);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        //监听班级焦点
        className.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    sign[1] = true;
                    String s = className.getText().toString();
                    textInputLayout_class.setError(CheckInput.isClassName(s));
                }
            }
        });
    }
    //对姓名输入进行预处理
    public void userName_proprecessor(){
        //监听姓名输入
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ret = CheckInput.isUserName(s.toString());
                flag[2] = ret.equals(" ");
                if(sign[2]){
                    textInputLayout_name.setError(ret);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        //监听姓名焦点变化
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    sign[2] = true;
                    String str = name.getText().toString();
                    textInputLayout_name.setError(CheckInput.isUserName(str));
                }
            }
        });
    }
    //对电话输入进行预处理
    public void phone_proprecessor(){
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ret = CheckInput.isPhonrNumber(s.toString());
                flag[3] = ret.equals(" ");
                if(sign[3]){
                    textInputLayout_phone.setError(ret);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    sign[3] = true;
                    String ret = phone.getText().toString();
                    textInputLayout_phone.setError(CheckInput.isPhonrNumber(ret));
                }
            }
        });
    }
    //对邮箱输入进行预处理
    public void email_proprecessor(){
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ret = CheckInput.isEmail(s.toString());
                flag[4] = ret.equals(" ");
                if(sign[4]){
                    textInputLayout_email.setError(ret);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    sign[4] = true;
                    String str = email.getText().toString();
                    textInputLayout_email.setError(CheckInput.isEmail(str));
                }
            }
        });
    }
    //对密码输入进行预处理
    public void passWord_proprecessor(){
        passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ret = CheckInput.isPassWord(s.toString());
                flag[5] = ret.equals(" ");
                textInputLayout_passWord.setError(ret);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        passWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String str = passWord.getText().toString();
                    sign[5] = true;
                    textInputLayout_passWord.setError(CheckInput.isPassWord(str));
                }
            }
        });
    }
    //对确认密码输入进行处理
    public void sure_passWord_proprecessor(){
        surePassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ret = CheckInput.isSurePassWord(s.toString());
                flag[6] = ret.equals(" ");
                if(sign[6]){
                    textInputLayout_surePassWord.setError(ret);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        surePassWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String str = surePassWord.getText().toString();
                    sign[6] = true;
                    textInputLayout_surePassWord.setError(CheckInput.isSurePassWord(str));
                }
            }
        });
    }
    //显示错误信息
    public void showErrorMessage(int i){
        if(!errorMassage.equals("")){
            errorMassage += "、";
        }
        switch (i){
            case 0:
                errorMassage += "学号";
                textInputLayout_id.setError(CheckInput.isStudentid(str_id));
                break;
            case 1:
                errorMassage += "班级";
                textInputLayout_class.setError(CheckInput.isClassName(str_class));
                break;
            case 2:
                errorMassage += "姓名";
                textInputLayout_name.setError(CheckInput.isUserName(str_name));
                break;
            case 3:
                errorMassage += "手机号码";
                textInputLayout_phone.setError(CheckInput.isPhonrNumber(str_phone));
                break;
            case 4:
                errorMassage += "邮箱";
                textInputLayout_email.setError(CheckInput.isEmail(str_email));
                break;
            case 5:
                errorMassage += "密码";
                textInputLayout_passWord.setError(CheckInput.isPassWord(str_passWord));
                break;
            case 6:
                errorMassage += "确认密码";
                textInputLayout_surePassWord.setError(CheckInput.isSurePassWord(str_sure));
                break;
        }
    }
    //注册
    public void userSignIn(){
        final SUser sUser = new SUser(str_id,str_college,Tools.getCollegeId(str_college),str_class,str_name,str_sex,str_phone,str_email,str_passWord,"a");
        new Thread(new Runnable() {
            @Override
            public void run() {
                int ret = DBUtils.insert(sUser);
                switch (ret){
                    case 0:
                        Looper.prepare();
                        mAlertDialog dialog = new mAlertDialog(SignInActivity.this,"提醒",getApplication().getString(R.string.attention1));
                        dialog.show();
                        Looper.loop();
                        break;
                    case 1:
                        Looper.prepare();
                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(SignInActivity.this);
                        dialog2.setMessage("注册成功！");
                        dialog2.setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent1 = new Intent(SignInActivity.this,LoginActivity.class);
                                startActivity(intent1);
                            }
                        });
                        dialog2.show();
                        Looper.loop();
                        finish();
                        break;
                    case 2:
                        Looper.prepare();
                        Toast.makeText(SignInActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        break;
                    case 3:
                        Looper.prepare();
                        Toast.makeText(SignInActivity.this,"系统异常，请联系管理员",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        break;
                    case 4:
                        Looper.prepare();
                        mAlertDialog mAlertDialog = new mAlertDialog(SignInActivity.this,"该学号已经注册","该学号已经被注册，若非本人操作，请联系系统管理员");
                        mAlertDialog.show();
                        Looper.loop();
                        break;
                }
            }
        }).start();
    }
    //核对注册信息
    public void checkUserInfo(){
        String str = "学号："+str_id+"\n";
        str += "学院："+str_college+"\n";
        str += "班级："+str_class+"\n";
        str += "姓名："+str_name+"\n";
        str += "性别："+str_sex+"\n";
        str += "手机号码："+str_phone+"\n";
        str += "电子邮箱："+str_email+"\n";
        AlertDialog.Builder dialog = new AlertDialog.Builder(SignInActivity.this);
        dialog.setTitle("请核对注册信息");
        dialog.setMessage(str);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userSignIn();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
