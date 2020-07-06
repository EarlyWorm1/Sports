package dhu.cst.yinqingbo416.sports.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class FragmentActivities extends BaseLazyFragment{
    private List<ActivityInfo>mActivityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private String collegeId;
    private String stuId;
    private SwipeRefreshLayout swipeRefresh;
    private ActivityAdapter activityAdapter;
    private List<Integer> applyActId = new ArrayList<>();
    private List<Integer> actIdList = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    activityAdapter = new ActivityAdapter(mActivityList);
                    recyclerView.setAdapter(activityAdapter);
                    swipeRefresh.setRefreshing(false);
                    break;
                case 2:
                    activityAdapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    break;
                case 4:
                    Toast.makeText(getContext(),"呀，没活动了！",Toast.LENGTH_SHORT).show();
                    //显示暂无活动的界面
                    swipeRefresh.setRefreshing(false);
                    break;
                default:
                    //数据加载失败
                    Toast.makeText(getContext(),"数据加载失败,请刷新重试！",Toast.LENGTH_SHORT).show();
                    //显示数据加载失败的界面
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        }
    };
    private TextView userName;
    public FragmentActivities() {}
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void loadDataStart() {//加载数据
        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("stuData",MODE_PRIVATE);
        collegeId = preferences.getString("collegeId"," ");
        stuId = preferences.getString("id"," ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ActivityInfo> actData = DBUtils.getActData(collegeId);
                mLoadDataFinished = true;
                Message msg = new Message();
                if(actData == null){
                    msg.what = 100;
                }else if(actData.size() == 0){
                    msg.what = 4;
                }else {
                    mActivityList = actData;
                    actIdList = getActIdList();
                    applyActId = DBUtils.getApplyActId(stuId,actIdList);
                    Tools.setApplyActId(applyActId);
                    if(applyActId == null){
                        msg.what = 100;
                    }else {
                        if(mViewInflateFinished){
                            msg.what = 1;
                        }
                    }
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    protected void findViewById(View view) {
        recyclerView = view.findViewById(R.id.fragment_activity_recycleView);
        swipeRefresh = view.findViewById(R.id.fa_swipeRefreshLayout);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecycleView();
            }
        });
        swipeRefresh.setRefreshing(true);
        userName = view.findViewById(R.id.fa_userName);
        userName.setText("Hi,"+Tools.userName);
        if(mLoadDataFinished){//数据已经加载好
            //recyclerView.setVisibility(View.VISIBLE);//显示recyclerView
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            ActivityAdapter activityAdapter = new ActivityAdapter(mActivityList);
            recyclerView.setAdapter(activityAdapter);
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activies,container,false);
    }

    //刷新操作
    public void refreshRecycleView(){
        new Thread(new Runnable() {
            @Override
            public void run() {//刷新数据
                List<ActivityInfo> actData = DBUtils.getActData(collegeId);
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
                applyActId = DBUtils.getApplyActId(stuId,actIdList);
                Tools.setApplyActId(applyActId);
                if(applyActId == null){
                    msg.what = 100;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    //获得已报名活动的编号
    public List<Integer> getActIdList(){
        List<Integer> data = new ArrayList<>();
        for(ActivityInfo item : mActivityList){
            data.add(item.getId());
        }
        return data;
    }

    //部分更新RecyclerView
    public void updateRecyclerView(int position,ActivityInfo act1){
        mActivityList.set(position,act1);
        activityAdapter.notifyDataSetChanged();
    }
}
