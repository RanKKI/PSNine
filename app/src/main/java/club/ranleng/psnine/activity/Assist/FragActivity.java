package club.ranleng.psnine.activity.Assist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import club.ranleng.psnine.model.KEY;
import club.ranleng.psnine.util.ViewGroupUtils;
import club.ranleng.psnine.view.ZoomImageView;

public class FragActivity extends BaseActivity implements ArticleListFragment.FinishLoadListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.framelayout) FrameLayout frameLayout;

    private String title;

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
    public void getData() {
        int key = getIntent().getIntExtra("key",-1);
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
            Glide.with(this).load(getIntent().getStringExtra("url")).into(imageView);
        }else if(key == KEY.NOTICE){
            Fragment f = new ArticleListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "notice");
            bundle.putBoolean("search",false);
            bundle.putString("key", null);
            f.setArguments(bundle);
            title = "短消息";
            openFragment(f);
        }
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
