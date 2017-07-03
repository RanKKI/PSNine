package club.ranleng.psnine.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import club.ranleng.psnine.activity.SettingActivity;
import club.ranleng.psnine.fragments.SettingFragment;


public abstract class BaseFragment extends Fragment {

    public Context mContext;
    protected View mRootView;
    protected boolean isVisible;
    private boolean isPrepared;
    private boolean isLoadData = false;

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
            mRootView = initView(inflater);
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
        //设置中的preload为true
//        if(!NetworkUtils.isNetworkAvailable(MainActivity.context)){
//            Toast.makeText(this.getContext(), "无网络链接", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if(isPrepared && SettingActivity.PREF_PRELOAD && !isLoadData){
            isLoadData = true;
            initData();
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

    public abstract View initView(LayoutInflater inflater);

    public abstract void initData();
}
