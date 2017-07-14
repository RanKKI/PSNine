package club.ranleng.psnine.activity.Main;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.Assist.AboutActivity;
import club.ranleng.psnine.activity.Assist.FragActivity;
import club.ranleng.psnine.activity.Assist.PickImgActivity;
import club.ranleng.psnine.activity.Post.NewGeneActivity;
import club.ranleng.psnine.activity.Post.NewTopicActivity;
import club.ranleng.psnine.adapter.ViewPagerAdapter.MainPagerAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.fragments.ArticleListFragment;
import club.ranleng.psnine.fragments.PSNGameListFragment;
import club.ranleng.psnine.model.KEY;
import club.ranleng.psnine.util.AndroidUtilCode.LogUtils;
import club.ranleng.psnine.util.AndroidUtilCode.Utils;
import club.ranleng.psnine.util.CrashHandler;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.util.PhoneUtils;
import club.ranleng.psnine.util.ReadFile;
import club.ranleng.psnine.widget.Requests.RequestClient;
import club.ranleng.psnine.widget.Requests.RequestGet;
import club.ranleng.psnine.widget.UserStatus;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ArticleListFragment.FinishLoadListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.main_fab) FloatingActionButton fab;

    private TextView nav_username;
    private ImageView nav_icon;
    private Context context;
    private String[] tabs_keys = {"gene", "topic", "openbox", "guide", "plus"};
    private ArrayList<Integer> when_login = new ArrayList<Integer>() {{
        add(R.id.nav_notice);
        add(R.id.nav_photo);
        add(R.id.nav_personal);
    }};
    private ArrayList<Integer> when_logout = new ArrayList<Integer>() {{
        add(R.id.nav_login);
    }};

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
        context = this;
        View headerLayout = navigationView.getHeaderView(0);
        nav_username = (TextView) headerLayout.findViewById(R.id.nav_header_username);
        nav_icon = (ImageView) headerLayout.findViewById(R.id.nav_header_icon);
    }

    @Override
    public void setupViews() {
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
        toggle.syncState();
        RequestClient.initOkhttpclient(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserStatus.isLogin()) {
                    MakeToast.plzlogin();
                    return;
                }
                String current_tab = tabs_keys[tabLayout.getSelectedTabPosition()];
                if (current_tab.contentEquals("gene")) {
                    startActivity(new Intent(context, NewGeneActivity.class));
                } else {
                    startActivity(new Intent(context, NewTopicActivity.class));
                }
            }
        });
        refresh_cache();
    }

    private void refresh_cache() {
        try {
            navigationView.getMenu().findItem(R.id.nav_cache).setTitle("缓存 " + PhoneUtils.getFormatSize(PhoneUtils.getFolderSize(getCacheDir().getAbsoluteFile())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getData() {
        PreferenceManager.setDefaultValues(this, R.xml.settings_general, false);
        KEY.initSetting(this);
        Utils.init(this);
        CrashHandler crashHandler = new CrashHandler();
        crashHandler.init(this);

        List<Fragment> fl = new ArrayList<>(); //填充要的Fragment頁卡

        Fragment f = new PSNGameListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("search", false);
        f.setArguments(bundle);

        fl.add(setup("gene"));
        fl.add(setup("topic"));
        fl.add(setup("openbox"));
        fl.add(setup("guide"));
        fl.add(setup("plus"));
        fl.add(f);
        if (viewPager != null) {
            viewPager.setAdapter(new MainPagerAdapter(getFragmentManager(), fl));  //設定Adapter給viewPager
        }
        tabLayout.setupWithViewPager(viewPager); //绑定viewPager

        final File file = new File(getFilesDir() + "/crash");
        if (file.exists()) {
            TextView textView = new TextView(this);
            textView.setTextSize(10);
            textView.setText(ReadFile.read("crash"));
            AlertDialog c = new AlertDialog.Builder(context)
                    .setView(textView)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            c.show();
        }
        if (file.delete()) {
            LogUtils.d("已删除");
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                Intent intent = new Intent(context, FragActivity.class);
                intent.putExtra("key", KEY.SEARCH);
                intent.putExtra("query", query);
                intent.putExtra("type", tabs_keys[tabLayout.getSelectedTabPosition()]);
                startActivity(intent);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.nav_notice) {
//            startActivity(new Intent(this, NoticeActivity.class));
            Intent intent = new Intent(this, FragActivity.class);
            intent.putExtra("key", KEY.NOTICE);
            startActivity(intent);
        } else if (id == R.id.nav_photo) {
            Intent intent = new Intent(this, PickImgActivity.class);
            intent.putExtra("form_main", true);
            startActivity(intent);
        } else if (id == R.id.nav_personal) {
            Intent intent = new Intent(this, PersonInfoActivity.class);
            intent.putExtra("psnid", UserStatus.getusername());
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, FragActivity.class);
            intent.putExtra("key", KEY.SETTING);
            startActivity(intent);
        } else if (id == R.id.nav_cache) {
            AlertDialog b = new AlertDialog.Builder(context)
                    .setTitle("确定要清除缓存么")
                    .setMessage("清除缓存虽然可以减少手机空间的占用, 但下次加载的时候会耗费更多的流量")
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (PhoneUtils.deleteDir(getCacheDir())) {
                                MakeToast.str("成功清除缓存");
                                refresh_cache();
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            b.show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Fragment setup(String i) {
        Fragment f = new ArticleListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", i);
        bundle.putBoolean("search", false);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onFinish() {
        if (!UserStatus.getdao()) {
            new RequestGet().execute("dao");
            MakeToast.str("签到成功");
            UserStatus.setdao(true);
        }
        Menu menu = navigationView.getMenu();
        if (UserStatus.isLogin()) {
            for (Integer i : when_login) {
                menu.findItem(i).setVisible(true);
            }
            for (Integer i : when_logout) {
                menu.findItem(i).setVisible(false);
            }
            nav_username.setText(UserStatus.getusername());
            Glide.with(this).load(UserStatus.getusericonurl()).into(nav_icon);
        } else {
            for (Integer i : when_logout) {
                menu.findItem(i).setVisible(true);
            }
            for (Integer i : when_login) {
                menu.findItem(i).setVisible(false);
            }
        }
    }
}
