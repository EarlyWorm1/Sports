package dhu.cst.yinqingbo416.sports;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import dhu.cst.yinqingbo416.sports.Entry.ActivityInfo;
import dhu.cst.yinqingbo416.sports.Entry.News;
import dhu.cst.yinqingbo416.sports.Fragment.FragmentActivities;
import dhu.cst.yinqingbo416.sports.Fragment.FragmentHistory;
import dhu.cst.yinqingbo416.sports.Fragment.FragmentMyself;
import dhu.cst.yinqingbo416.sports.Fragment.FragmentNews;
import dhu.cst.yinqingbo416.sports.Utils.Tools;

public class MainActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private Fragment mFragment;
    private FragmentActivities fa;
    private FragmentHistory fh;
    private FragmentNews fn;
    private FragmentMyself fm;
    private String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();//一些控件和数据的初始化
        initFragment();//初始化fragment
        Connector.getDatabase();//创建数据库
        //Log.d(TAG, "数据库创建成功！");
        List<News>newsList = DataSupport.findAll(News.class);
        //Log.d(TAG, "数据查询成功！");
        if(newsList == null || newsList.size() == 0){
            Tools.initTestDB();
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.main_activities:
                        Tools.currentPageNum = 1;
                        Tools.loadPage[0] = true;
                        replaceFragment(fa);
                        break;
                    case R.id.main_histroy:
                        Tools.loadPage[1] = true;
                        Tools.currentPageNum = 2;
                        replaceFragment(fh);
                        break;
                    case R.id.main_news:
                        Tools.currentPageNum = 3;
                        replaceFragment(fn);
                        break;
                    case R.id.main_my:
                        Tools.currentPageNum = 4;
                        replaceFragment(fm);
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        switch (Tools.currentPageNum){
            case 1:
                Tools.loadPage[0] = true;
                replaceFragment(fa);
                break;
            case 2:
                replaceFragment(fh);
                break;
            case 3:
                replaceFragment(fn);
                break;
            case 4:
                replaceFragment(fm);
                break;
            default:
                break;
        }
        //replaceFragment(fa);
        super.onStart();
    }

    //替换Fragment
    private void replaceFragment(Fragment fragment){
        if(fragment != mFragment){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if(mFragment == null){
                transaction.show(fragment).commit();
            }else {
                transaction.hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
        }
    }
    //初始化fragment界面
    public void initFragment(){
        mFragment = null;
        fa = new FragmentActivities();
        fh = new FragmentHistory();
        fn = new FragmentNews();
        fm = new FragmentMyself();
        //将各个fragment添加并隐藏起来
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_fragment,fh);
        transaction.hide(fh);
        transaction.add(R.id.main_fragment,fa);
        transaction.hide(fa);
        transaction.add(R.id.main_fragment,fn);
        transaction.hide(fn);
        transaction.add(R.id.main_fragment,fm);
        transaction.hide(fm);
        transaction.commit();
    }
    //初始化函数
    public void init(){
        radioGroup = findViewById(R.id.main_radiogroup);
        SharedPreferences preferences = getSharedPreferences("stuData",MODE_PRIVATE);
        Tools.userName = preferences.getString("name","");
        Tools.userId = preferences.getString("id"," ");
        Tools.collegeId = preferences.getString("collegeId"," ");
        fragmentManager = getSupportFragmentManager();
    }

    //接收活动详情页面的返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK){
                    int position = data.getIntExtra("Position",-1);
                    ActivityInfo act = (ActivityInfo) data.getSerializableExtra("Act");
                    boolean isOption = data.getBooleanExtra("isOption",false);
                    if(position != -1 && isOption){
                        fa.updateRecyclerView(position,act);
                        if(Tools.currentPageNum == 1){
                            if(Tools.loadPage[1]){
                                fh.refreshRecycleView();
                            }
                        }else if(Tools.currentPageNum == 2){
                            fh.removeAct(position);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

}
