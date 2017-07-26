package club.ranleng.psnine.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import club.ranleng.psnine.adapter.MainActivityPagerAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.event.LoadEvent;
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
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.main_root) CoordinatorLayout root;

    private int[] tabs_keys = {KEY.TYPE_GENE, KEY.TYPE_TOPIC, KEY.TYPE_OPENBOX, KEY.TYPE_GUIDE, KEY.TYPE_PLUS};

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserStatus.isLogin()) {
                    MakeToast.plzlogin();
                    return;
                }
                if (tabs_keys[tabLayout.getSelectedTabPosition()] == KEY.TYPE_GENE) {
                    startActivity(new Intent(context, newGeneActivity.class));
                } else {
                    startActivity(new Intent(context, newTopicActivity.class));
                }
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void getData() {
        Utils.init(this);
        Internet.init();
        KEY.initSetting(this);
        EventBus.getDefault().register(this);

        bindViewPager();
        refresh_cache();
    }

    private void bindViewPager(){
        viewPager.setAdapter(new MainActivityPagerAdapter(getFragmentManager()));  //設定Adapter給viewPager
        tabLayout.setupWithViewPager(viewPager); //绑定viewPager
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                Intent intent = new Intent(MainActivity.this, FragActivity.class);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
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

        if (id == R.id.nav_login) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.nav_photo) {
            Intent intent = new Intent(this, ImageGalleryActivity.class);
            intent.putExtra("from_main", true);
            startActivity(intent);
        } else if (id == R.id.nav_notice) {
            Intent intent = new Intent(this, FragActivity.class);
            intent.putExtra("key", KEY.TYPE_NOTICE);
            startActivity(intent);
        } else if (id == R.id.nav_personal) {
            Intent intent = new Intent(this, PSNActivity.class);
            intent.putExtra("psnid", UserStatus.getusername());
            startActivity(intent);
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
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
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
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
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

        if(!UserStatus.isLogin()){
            nav_icon.setImageResource(R.mipmap.psnine);
            nav_username.setText("PSNINE");
            return;
        }

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

    interface User {
        @GET("set/qidao/ajax")
        Call<ResponseBody> Dao();
    }
}
