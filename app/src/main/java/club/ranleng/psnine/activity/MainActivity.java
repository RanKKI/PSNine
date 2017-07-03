package club.ranleng.psnine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.ViewPagerAdapter.MainPagerAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.fragments.ArticleListFragment;
import club.ranleng.psnine.widget.Requests.RequestClient;
import club.ranleng.psnine.widget.UserStatus;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ArticleListFragment.FinishLoadListener{

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView nav_username;
    private ImageView nav_icon;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
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
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        RequestClient.initOkhttpclient(this);
    }

    @Override
    public void getData() {
        PreferenceManager.setDefaultValues(this, R.xml.settings_general, false);
        SettingActivity.initSetting(this);
    }

    @Override
    public void showContent() {
        List<Fragment> fl = new ArrayList<>(); //填充要的Fragment頁卡
        fl.add(setup("gene"));
        fl.add(setup("topic"));
        fl.add(setup("openbox"));
        fl.add(setup("guide"));
        fl.add(setup("plus"));
        if (viewPager != null) {
            viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fl));  //設定Adapter給viewPager
        }
        tabLayout.setupWithViewPager(viewPager); //绑定viewPager
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_login){
            startActivity(new Intent(this, LoginActivity.class));
        }else if(id == R.id.nav_notice){
            startActivity(new Intent(this, NoticeActivity.class));
        }else if(id == R.id.nav_photo){
            startActivity(new Intent(this, PickImgActivity.class));
        }else if(id == R.id.nav_personal){
            Intent intent = new Intent(this, PersonInfoActivity.class);
            intent.putExtra("psnid", UserStatus.getusername());
            startActivity(intent);
        }else if(id == R.id.nav_about){
            startActivity(new Intent(this, AboutActivity.class));
        }else if(id == R.id.nav_setting){
            startActivity(new Intent(this, SettingActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Fragment setup(String i) {
        Fragment f = new ArticleListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", i);
        f.setArguments(bundle);
        return f;
    }

    private ArrayList<Integer> when_login = new ArrayList<Integer>(){{
        add(R.id.nav_notice);
        add(R.id.nav_photo);
        add(R.id.nav_personal);
    }};

    private ArrayList<Integer> when_logout = new ArrayList<Integer>(){{
        add(R.id.nav_login);
    }};
    @Override
    public void onFinish() {
        Menu menu = navigationView.getMenu();
        if(UserStatus.isLogin()){
            for(Integer i : when_login){
                menu.findItem(i).setVisible(true);
            }
            for(Integer i : when_logout){
                menu.findItem(i).setVisible(false);
            }
            nav_username.setText(UserStatus.getusername());
            Glide.with(this).load(UserStatus.getusericonurl()).into(nav_icon);
        }else{
            for(Integer i : when_logout){
                menu.findItem(i).setVisible(true);
            }
            for(Integer i : when_login){
                menu.findItem(i).setVisible(false);
            }
        }
    }
}
