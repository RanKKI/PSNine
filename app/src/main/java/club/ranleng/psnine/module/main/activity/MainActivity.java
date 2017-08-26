package club.ranleng.psnine.module.main.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.common.UserState;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.module.login.LoginActivity;
import club.ranleng.psnine.module.newtopic.NewTopicActivity;
import club.ranleng.psnine.utils.EmojiUtils;
import club.ranleng.psnine.utils.LocalFile;
import club.ranleng.psnine.view.RoundImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainContract.View {


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.main_root) CoordinatorLayout root;
    @BindView(R.id.app_bar) AppBarLayout appbar;
    @BindView(R.id.fab) FloatingActionButton fab;


    private MainContract.Presenter mPresenter;
    private TextView nav_username;
    private RoundImageView nav_icon;
    private Menu nav_menu;

    private Fragment current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        toolbar.setLayoutParams(params);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        View headerLayout = navigationView.getHeaderView(0);
        nav_username = (TextView) headerLayout.findViewById(R.id.nav_header_username);
        nav_icon = (RoundImageView) headerLayout.findViewById(R.id.nav_header_icon);
        nav_menu = navigationView.getMenu();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.FabClick();
            }
        });

        Utils.init(this);
        ApiManager.init();
        KEY.initSetting();
        EmojiUtils.init();
        new MainPresenter(this);

        mPresenter.start();
        refresh_cache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!mPresenter.isMain(current)) {
            mPresenter.openMain();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        appbar.setExpanded(true);

        if (id == R.id.nav_index) {
            mPresenter.openMain();
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.nav_photo) {
            mPresenter.openPhoto();
        } else if (id == R.id.nav_notice) {
            mPresenter.openNotice();
        } else if (id == R.id.nav_personal) {
            mPresenter.openPSN();
        } else if (id == R.id.nav_element) {
            mPresenter.openEle();
        } else if (id == R.id.nav_setting) {
            mPresenter.openSetting();
        } else if (id == R.id.nav_cache) {
            mPresenter.Cache();
        } else if (id == R.id.nav_about) {
            mPresenter.openAbout();
        } else if (id == R.id.nav_fav) {
            mPresenter.openFav();
        } else if (id == R.id.nav_logout) {
            mPresenter.Logout();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void set_menu(boolean b) {
        nav_menu.setGroupVisible(R.id.user_root, b);
        nav_menu.findItem(R.id.nav_logout).setVisible(b);
        nav_menu.findItem(R.id.nav_login).setVisible(!b);
    }

    private void show_snackBar(String s) {
        Snackbar.make(root, s, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setNotLogin() {
        nav_username.setText(getString(R.string.app_name));
        nav_icon.setImageResource(R.mipmap.psnine);
        set_menu(false);
    }

    @Override
    public void setLogin() {
        set_menu(true);
    }

    @Override
    public void setUsername(String username) {
        nav_username.setText(username);
    }

    @Override
    public void setUserIcon(String icon_src) {
        Glide.with(this).load(icon_src).asBitmap().into(nav_icon);
    }

    @Override
    public void newMsg() {
        show_snackBar(getString(R.string.newMsg));
    }

    @Override
    public void onSignin() {
        show_snackBar(getString(R.string.Signin));
    }

    @Override
    public void openFragment(Fragment fragment, String title) {
        setTitle(title);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        fabControl(mPresenter.isMain(current));
        if (current == null) {
            current = fragment;
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
            return;
        }

        if (current != fragment) {
            if (!fragment.isAdded()) {// 先判断是否被add过
                transaction.hide(current).add(R.id.frameLayout, fragment); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(current).show(fragment); // 隐藏当前的fragment，显示下一个
            }
            current = fragment;
        }
        transaction.commit();
    }

    @Override
    public void newTopic(int type) {
        Intent intent = new Intent(this, NewTopicActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @Override
    public void fabControl(Boolean visible) {
        if (!UserState.isLogin()) {
            visible = false;
        }
        fab.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showCacheDialog() {
        new AlertDialog.Builder(this)
                .setTitle("确定要清除缓存么")
                .setMessage("清除缓存虽然可以减少手机空间的占用, 但下次加载的时候会耗费更多的流量")
                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (LocalFile.deleteDir(getCacheDir())) {
                            show_snackBar("成功清除缓存");
                            refresh_cache();
                        }
                    }
                })
                .setNegativeButton("取消", null).create().show();
    }

    private void refresh_cache() {
        try {
            nav_menu.findItem(R.id.nav_cache).setTitle("缓存 " + LocalFile.getFormatSize(LocalFile.getFolderSize(getCacheDir().getAbsoluteFile())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
