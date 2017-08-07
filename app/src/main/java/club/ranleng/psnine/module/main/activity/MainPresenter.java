package club.ranleng.psnine.module.main.activity;

import android.app.Fragment;

import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.common.RxBus;
import club.ranleng.psnine.common.UserState;
import club.ranleng.psnine.common.event.TabsChange;
import club.ranleng.psnine.common.event.UserInfoLoad;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.module.element.ElementFragment;
import club.ranleng.psnine.module.setting.SettingFragment;
import club.ranleng.psnine.module.topics.TopicsTabsFragment;
import club.ranleng.psnine.module.about.AboutFragment;
import club.ranleng.psnine.module.photo.PhotoGalleryFragment;
import club.ranleng.psnine.module.topics.TopicsFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {

    private final MainContract.View mMainView;
    private Disposable login_event;
    private Disposable tabs_change_event;

    private Fragment main;
    private Fragment psn;
    private Fragment notice;
    private Fragment photos;
    private Fragment setting;
    private Fragment element;
    private Fragment fav;

    public MainPresenter(MainContract.View view) {
        this.mMainView = view;
        this.mMainView.setPresenter(this);
    }

    @Override
    public void start() {
        login_event = RxBus.getDefault().toObservable(UserInfoLoad.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserInfoLoad>() {
                    @Override
                    public void accept(@NonNull UserInfoLoad userInfoLoad) throws Exception {
                        if(!UserState.isLogin()){
                            mMainView.setNotLogin();
                            return;
                        }
                        mMainView.setLogin();
                        mMainView.setUsername(userInfoLoad.getName());
                        mMainView.setUserIcon(userInfoLoad.getIcon());
                        if (!userInfoLoad.getDao()) {
                            ApiManager.getDefault().signIn();
                            mMainView.onSignin();
                        }

                        if (userInfoLoad.getMsg()) {
                            mMainView.newMsg();
                        }
                    }
                });
        tabs_change_event = RxBus.getDefault().toObservable(TabsChange.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TabsChange>() {
                    @Override
                    public void accept(@NonNull TabsChange tabsChange) throws Exception {
                        if(tabsChange.getChange()){
                            main = null;
                            tabsChange.setChange(false);
                        }
                    }
                });

        openMain();
    }

    @Override
    public void onDestory() {
        login_event.dispose();
        tabs_change_event.dispose();
    }

    @Override
    public void openMain() {
        if (main == null) {
            main = TopicsTabsFragment.newInstance();
        }
        mMainView.openFragment(main, "首页");
    }

    @Override
    public void openPSN() {

    }

    @Override
    public void openNotice() {
        if(notice == null){
            notice = TopicsFragment.newInstance(KEY.NOTICE,null,null);
        }
        mMainView.openFragment(notice, "短消息");
    }

    @Override
    public void openEle() {
        if(element == null){
            element = ElementFragment.newInstance();
        }
        mMainView.openFragment(element, "元素");
    }

    @Override
    public void openFav() {

    }

    @Override
    public void openPhoto() {
        if(photos == null){
            photos = PhotoGalleryFragment.newInstance();
        }
        mMainView.openFragment(photos, "图库");
    }

    @Override
    public void openSetting() {
        if(setting == null){
            setting = SettingFragment.newInstance();
        }
        mMainView.openFragment(setting, "设置");
    }

    @Override
    public void openAbout() {
        mMainView.openFragment(AboutFragment.newInstance(), "关于");
    }

    @Override
    public void Logout() {
        UserState.setIsLogin(false);
        ApiManager.clear();
        RxBus.getDefault().send(new UserInfoLoad());
    }


    @Override
    public Boolean isMain(Fragment fragment) {
        return fragment != main;
    }
}
