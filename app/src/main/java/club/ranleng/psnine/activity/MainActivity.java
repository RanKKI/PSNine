package club.ranleng.psnine.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.post.newGeneActivity;
import club.ranleng.psnine.activity.post.newTopicActivity;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.event.LoadEvent;
import club.ranleng.psnine.fragment.ListItemFragment;
import club.ranleng.psnine.fragment.main.MainPSNFragment;
import club.ranleng.psnine.fragment.main.MainTabsFragment;
import club.ranleng.psnine.utils.LocalFile;
import club.ranleng.psnine.utils.MakeToast;
import club.ranleng.psnine.widget.Internet;
import club.ranleng.psnine.widget.KEY;
import club.ranleng.psnine.widget.UserStatus;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.main_root) CoordinatorLayout root;


    private TextView nav_username;
    private ImageView nav_icon;
    private ArrayList<Integer> when_login = new ArrayList<Integer>() {{
        add(R.id.nav_notice);
        add(R.id.nav_photo);
        add(R.id.nav_personal);
        add(R.id.nav_logout);
    }};
    private ArrayList<Integer> when_logout = new ArrayList<Integer>() {{
        add(R.id.nav_login);
    }};
    private Context context;
    private Fragment current;

    private Fragment main;
    private Fragment psn;
    private Fragment notice;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
        View headerLayout = navigationView.getHeaderView(0);
        nav_username = (TextView) headerLayout.findViewById(R.id.nav_header_username);
        nav_icon = (ImageView) headerLayout.findViewById(R.id.nav_header_icon);
    }

    @Override
    public void setupViews() {
        context = this;
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        main = new MainTabsFragment();
        openFragment(main, "首页");
    }

    @Override
    public void getData() {
        Utils.init(this);
        Internet.init();
        KEY.initSetting(this);
        EventBus.getDefault().register(this);
        refresh_cache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void refresh_cache() {
        try {
            navigationView.getMenu().findItem(R.id.nav_cache).setTitle("缓存 " + LocalFile.getFormatSize(LocalFile.getFolderSize(getCacheDir().getAbsoluteFile())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_index) {
            openFragment(main, "首页");
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.nav_photo) {
            Intent intent = new Intent(this, ImageGalleryActivity.class);
            intent.putExtra("from_main", true);
            startActivity(intent);
        } else if (id == R.id.nav_notice) {
            if(notice == null){
                notice = ListItemFragment.newInstance(KEY.TYPE_NOTICE,null);
            }
            openFragment(notice,"短消息");
        } else if (id == R.id.nav_personal) {
            if (psn == null) {
                psn = MainPSNFragment.newInstance(UserStatus.getusername());
            }
            openFragment(psn, UserStatus.getusername());
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, FragActivity.class);
            intent.putExtra("key", KEY.SETTING);
            startActivity(intent);
        } else if (id == R.id.nav_cache) {
            new AlertDialog.Builder(context)
                    .setTitle("确定要清除缓存么")
                    .setMessage("清除缓存虽然可以减少手机空间的占用, 但下次加载的时候会耗费更多的流量")
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (LocalFile.deleteDir(getCacheDir())) {
                                MakeToast.str("成功清除缓存");
                                refresh_cache();
                            }
                        }
                    })
                    .setNegativeButton("取消", null).create().show();
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(context)
                    .setTitle("登出")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.putExtra("logout", true);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", null).create().show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe
    public void onLoadEvent(LoadEvent loadEvent) {

        Menu menu = navigationView.getMenu();

        for (Integer i : when_login) {
            menu.findItem(i).setVisible(UserStatus.isLogin());
        }
        for (Integer i : when_logout) {
            menu.findItem(i).setVisible(!UserStatus.isLogin());
        }

        if (!UserStatus.isLogin()) {
            nav_icon.setImageResource(R.mipmap.psnine);
            nav_username.setText("PSNINE");
            return;
        }

        invalidateOptionsMenu();

        if (!UserStatus.getdao()) {
            User user = Internet.retrofit.create(User.class);
            user.Dao().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            LogUtils.d(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        MakeToast.str("签到成功");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
            UserStatus.setdao(true);
        }

        if (UserStatus.getNotice()) {
            Snackbar.make(root, "有新消息", Snackbar.LENGTH_LONG).setAction("确定", null).show();
            UserStatus.setNotice(false);
        }

        if (UserStatus.isLogin()) {
            nav_username.setText(UserStatus.getusername());
            Glide.with(this).load(UserStatus.getusericonurl()).into(nav_icon);
        }
    }

    private void openFragment(Fragment to, String title) {
        setTitle(title);
        if (current == null) {
            LogUtils.d("current is null, add");
            current = main;
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame, current)
                    .commit();
            return;
        }
        if (current != to) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(current).add(R.id.frame, to); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(current).show(to); // 隐藏当前的fragment，显示下一个
            }
            transaction.commit();
            current = to;
        }
    }

    interface User {
        @GET("set/qidao/ajax")
        Call<ResponseBody> Dao();
    }
}
