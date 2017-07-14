package club.ranleng.psnine.activity.Assist;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.Listener.TouchListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.fragments.ArticleListFragment;
import club.ranleng.psnine.fragments.SettingFragment;
import club.ranleng.psnine.fragments.TrophyFragment;
import club.ranleng.psnine.fragments.TrophyTipsFragment;
import club.ranleng.psnine.model.KEY;
import club.ranleng.psnine.util.AndroidUtilCode.LogUtils;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.util.ViewGroupUtils;
import club.ranleng.psnine.view.ZoomImageView;

public class FragActivity extends BaseActivity implements ArticleListFragment.FinishLoadListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.framelayout) FrameLayout frameLayout;

    private String title;
    private int key;
    private String url;

    @Override
    public void setContentView() {
        setContentView(R.layout.toolbar_framelayout);
    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_frag, menu);
        if(key == KEY.IMAGE){
            menu.findItem(R.id.action_frag_saveimg).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_frag_saveimg){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                    int REQUEST_PERMISSION_READ_EXTERNAL = 21;
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL);
                }else{
                    SaveImg();
                }
            }else{
                SaveImg();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void SaveImg(){
        MakeToast.str("没有很想写这部分 (不要着急  以后会增加上去的");
    }

    @Override
    public void getData() {
        key = getIntent().getIntExtra("key",-1);
        if(key == KEY.SEARCH){
            Fragment f = new ArticleListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", getIntent().getStringExtra("type"));
            bundle.putBoolean("search",true);
            bundle.putString("key", getIntent().getStringExtra("query"));
            f.setArguments(bundle);
            title = getIntent().getStringExtra("query");
            openFragment(f);
        }else if (key == KEY.SETTING){
            Fragment f = new SettingFragment();
            title = "设置";
            openFragment(f);
        }else if(key == KEY.IMAGE){
            ZoomImageView imageView = new ZoomImageView(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setOnTouchListener(new TouchListener(imageView));
            ViewGroupUtils.replaceView(frameLayout, imageView);
            url = getIntent().getStringExtra("url");
            Glide.with(this).load(url).into(imageView);
        }else if(key == KEY.NOTICE){
            Fragment f = new ArticleListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "notice");
            bundle.putBoolean("search",false);
            bundle.putString("key", null);
            f.setArguments(bundle);
            title = "短消息";
            openFragment(f);
        }else if( key == KEY.TROPHYTIPS){
            title =  getIntent().getStringExtra("trophy_id");
            Fragment f = new TrophyTipsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("trophy_id", title);
            f.setArguments(bundle);
            openFragment(f);
        }else if(key == KEY.TROPHY){
            Fragment f = new TrophyFragment();
            String game_id = getIntent().getStringExtra("game_id");
            title = game_id;
            String username = getIntent().getStringExtra("username");
            if(username != null){
                title += "-" + username;
            }
            Bundle bundle = new Bundle();
            bundle.putString("game_id", game_id);
            bundle.putString("username",username);
            f.setArguments(bundle);
            openFragment(f);
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause(){
        super.onPause();
        KEY.initSetting(this);
    }

    private void openFragment(Fragment f){
        setTitle(title);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.framelayout, f)
                .commit();
    }

    @Override
    public void onFinish() {
        //DO NOTHING
    }
}
