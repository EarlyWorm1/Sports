package dhu.cst.yinqingbo416.sports.Fragment;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dhu.cst.yinqingbo416.sports.Adapter.ActivityAdapter;
import dhu.cst.yinqingbo416.sports.Entry.ActivityInfo;
import dhu.cst.yinqingbo416.sports.R;
import dhu.cst.yinqingbo416.sports.Utils.DBUtils;
import dhu.cst.yinqingbo416.sports.Utils.Tools;

import static android.content.Context.MODE_PRIVATE;

public class FragmentHistory extends BaseLazyFragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;//下拉刷新
    private List<ActivityInfo>mActivityList = new ArrayList<>();
    private ActivityAdapter activityAdapter;
    private String stuId;//学生学号
    private String collegeId;//学生学院号
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    activityAdapter = new ActivityAdapter(mActivityList);
                    recyclerView.setAdapter(activityAdapter);
                    refreshLayout.setRefreshing(false);
                    break;
                case 2:
                    //activityAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(activityAdapter);
                    refreshLayout.setRefreshing(false);
                    break;
                case 4:
                    Toast.makeText(getContext(),"还没有报名活动！",Toast.LENGTH_SHORT).show();
                    //显示暂无活动的界面
                    refreshLayout.setRefreshing(false);
                    break;
                default:
                    //数据加载失败
                    Toast.makeText(getContext(),"数据加载失败,请刷新重试！",Toast.LENGTH_SHORT).show();
                    //显示数据加载失败的界面
                    refreshLayout.setRefreshing(false);
                    break;
            }
        }
    };
    private TextView userNme;
    public FragmentHistory() {}//构造函数
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void loadDataStart() {//请求数据
        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("stuData",MODE_PRIVATE);
        stuId = preferences.getString("id"," ");
        collegeId = preferences.getString("collegeId"," ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ActivityInfo> actData = DBUtils.getUserApplyActivities(stuId,collegeId);
                //Log.d("FH", "活动数量："+actData.size());
                mLoadDataFinished = true;
                Message msg = new Message();
                if(actData == null){
                    msg.what = 100;
                }else if(actData.size() == 0){
                    msg.what = 4;
                }else {
                    mActivityList = actData;
                    if(mViewInflateFinished){
                        msg.what = 1;
                    }
                }
                handler.sendMessage(msg);
            }
        }).start();
    }
    @Override
    protected void findViewById(View view) {
        recyclerView = view.findViewById(R.id.fh_recyclerView);
        refreshLayout = view.findViewById(R.id.fh_swipeRefreshLayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecycleView();
            }
        });
        refreshLayout.setRefreshing(true);
        userNme = view.findViewById(R.id.fh_userName);
        userNme.setText("Hi,"+Tools.userName);
        if(mLoadDataFinished){//数据已经加载好
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            ActivityAdapter activityAdapter = new ActivityAdapter(mActivityList);
            recyclerView.setAdapter(activityAdapter);
        }
    }

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history,container,false);
    }
    //刷新操作
    public void refreshRecycleView(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ActivityInfo> actData = DBUtils.getUserApplyActivities(stuId,collegeId);
                Message msg = new Message();
                if(actData == null){
                    msg.what = 100;
                }else if(actData.size() == 0){
                    msg.what = 4;
                }
                else {
                    mActivityList.clear();
                    mActivityList.addAll(actData);
                    msg.what = 2;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }
    //部分更新RecyclerView
    public void updateRecyclerView(int position,ActivityInfo act1){
        mActivityList.set(position,act1);
        activityAdapter.notifyDataSetChanged();
    }
    //移除某位置报名的活动
    public void removeAct(int position){
        mActivityList.remove(position);
        activityAdapter.notifyDataSetChanged();
    }
}
