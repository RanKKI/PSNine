package club.ranleng.psnine.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import club.ranleng.psnine.common.KEY;

public abstract class BaseFragment extends Fragment {


    public Context mContext;
    protected View mRootView;
    protected boolean isVisible;
    private boolean isPrepared;
    private boolean isLoadData = false;
    private boolean isResume = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = initView(inflater, container);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    /**
     * 懒加载
     */
    protected void lazyLoad() {
//        if(!NetworkUtils.isConnected()){
//            MakeToast.str("无网络连接");
//            return;
//        }

        if (isPrepared && KEY.PREF_PRELOAD && !isLoadData) {
            isLoadData = true;
            initData();
            return;
        }

        if (!isPrepared || !isVisible) {
            return;
        }

        if (!isLoadData) {
            isLoadData = true;
            initData();
        }

    }

    // 不可见
    protected void onInvisible() {

    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container);

    public abstract void initData();

}
