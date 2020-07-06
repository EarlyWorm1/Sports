package dhu.cst.yinqingbo416.sports.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseLazyFragment extends Fragment {
    public boolean mHaveLoadData; // 表示是否已经请求过数据

    public boolean mLoadDataFinished; // 表示数据是否已经请求完毕
    private View mRootView;

    // 表示开始加载数据, 但不表示数据加载已经完成
    public abstract void loadDataStart();

    // 表示找控件完成, 给控件们设置数据不会报空指针了
    public boolean mViewInflateFinished;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mRootView != null) {
            return mRootView;
        }
        mRootView = initRootView(inflater, container, savedInstanceState);
        findViewById(mRootView);
        mViewInflateFinished = true;
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("BF", "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract void findViewById(View view);

    protected abstract View initRootView(LayoutInflater inflater, ViewGroup container,
                                         Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        // 如果还没有加载过数据 && 用户切换到了这个fragment
//        // 那就开始加载数据
//        Log.d("BF", "调用setUserVisibleHint函数");
//        if (!mHaveLoadData && isVisibleToUser) {
//            loadDataStart();
//            mHaveLoadData = true;
//        }
//    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden){
            //Log.d("BF", "fragment隐藏");
        }else {
            //Log.d("BF", "fragment显示");
            if (!mHaveLoadData) {
                loadDataStart();
                mHaveLoadData = true;
            }
        }
        super.onHiddenChanged(hidden);
    }
}