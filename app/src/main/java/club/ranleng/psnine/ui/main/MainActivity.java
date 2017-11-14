package club.ranleng.psnine.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.common.RxBus;
import club.ranleng.psnine.common.UserState;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.model.UserInfo;
import club.ranleng.psnine.ui.imageGallery.ImagesGalleryActivity;
import club.ranleng.psnine.ui.LoginActivity;
import club.ranleng.psnine.ui.newTopic.newTopicActivity;
import club.ranleng.psnine.ui.setting.SettingsActivity;
import club.ranleng.psnine.ui.topics.TopicsActivity;
import club.ranleng.psnine.utils.CacheUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.fab) FloatingActionButton fab;

    private Disposable userInfo;
    private TextView nav_username;
    private ImageView nav_avatar;
    private Context context;
    private mViewPagerAdapter mViewPagerAdapter;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
        context = this;
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        nav_username = navigationView.getHeaderView(0).findViewById(R.id.nav_username);
        nav_avatar = navigationView.getHeaderView(0).findViewById(R.id.nav_avatar);
        navigationView.getMenu().findItem(R.id.nav_notice).setActionView(R.layout.nav_menu_badge);
        assert nav_avatar != null;
        assert nav_username != null;
        mViewPagerAdapter = new mViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        fab.setVisibility(UserState.isLogin() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void getData() {
        Key.getSetting();
        refreshCache();
        userInfo = RxBus.getDefault().toObservable(UserInfo.class)
                .subscribe(new updateUserInfo());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userInfo.dispose();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        if (id == R.id.nav_login) {
            ActivityUtils.startActivity(LoginActivity.class);
        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this).setMessage(R.string.ask_to_logout)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ApiManager.getDefault().logout();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null).create().show();
        } else if (id == R.id.nav_cache) {
            new AlertDialog.Builder(this).setTitle(R.string.ask_to_clean_cache)
                    .setMessage(R.string.ask_to_clean_cache_message)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CacheUtils.deleteDir(getCacheDir().getAbsoluteFile());
                            refreshCache();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null).create().show();
        } else if (id == R.id.nav_setting) {
            ActivityUtils.startActivity(SettingsActivity.class);
        } else if (id == R.id.nav_notice) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", Key.NOTICE);
            ActivityUtils.startActivity(bundle, TopicsActivity.class);
        } else if (id == R.id.nav_photo) {
            ActivityUtils.startActivity(ImagesGalleryActivity.class);
        }
        return true;
    }

    private void refreshCache() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String cache_count = CacheUtils.getCount(getCacheDir().getAbsoluteFile());
                String cache = getString(R.string.nav_cache) + " " + cache_count;
                e.onNext(cache);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        navigationView.getMenu().findItem(R.id.nav_cache).setTitle(s);
                    }
                });

    }

    @OnClick(R.id.fab)
    public void newTopic() {
        int position = tabLayout.getSelectedTabPosition();
        int type = mViewPagerAdapter.getKeyByPosition(position);
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        ActivityUtils.startActivity(bundle, newTopicActivity.class);
    }

    class updateUserInfo implements Consumer<UserInfo> {

        @Override
        public void accept(UserInfo userInfo) throws Exception {
            if (!userInfo.getNotice()) {
                ToastUtils.showShort(R.string.new_reply);
            }
            View notice_badge = navigationView.getMenu().findItem(R.id.nav_notice).getActionView();
            notice_badge.setVisibility(!userInfo.getNotice() ? View.VISIBLE : View.INVISIBLE);

            if (UserState.isLogin()) {
                return;
            }
            if (userInfo.getUsername() != null) {
                UserState.setLogin(true);
                UserState.setUserInfo(userInfo);
                Glide.with(context).load(userInfo.getAvatar()).into(nav_avatar);
                nav_username.setText(userInfo.getUsername());
                if (!userInfo.getSign()) {
                    ApiManager.getDefault().Signin();
                }
            } else {
                nav_username.setText(R.string.not_log_in);
            }
            Menu menu = navigationView.getMenu();
            menu.setGroupVisible(R.id.user_root, UserState.isLogin());
            menu.findItem(R.id.nav_login).setVisible(!UserState.isLogin());
            menu.findItem(R.id.nav_logout).setVisible(UserState.isLogin());
            fab.setVisibility(UserState.isLogin() ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
