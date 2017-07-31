package club.ranleng.psnine.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.Listener.TouchListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.fragment.ListItemFragment;
import club.ranleng.psnine.fragment.TrophyFragment;
import club.ranleng.psnine.fragment.TrophyTipsFragment;
import club.ranleng.psnine.utils.MakeToast;
import club.ranleng.psnine.utils.ViewGroupUtils;
import club.ranleng.psnine.view.ZoomImageView;
import club.ranleng.psnine.widget.KEY;

public class FragActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.framelayout) FrameLayout frameLayout;

    private String title;
    private int key;

    @Override
    public void setContentView() {
        setContentView(R.layout.view_toolbar_framelayout);
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
        if (key == KEY.IMAGE) {
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

        if (id == R.id.action_frag_saveimg) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                    int REQUEST_PERMISSION_READ_EXTERNAL = 21;
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL);
                } else {
                    SaveImg();
                }
            } else {
                SaveImg();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void SaveImg() {
        MakeToast.str("没有很想写这部分 (不要着急  以后会增加上去的");
    }

    @Override
    public void getData() {
        key = getIntent().getIntExtra("key", -1);
        if (key == KEY.SEARCH) {
            title = getIntent().getStringExtra("query");
            openFragment(ListItemFragment.newInstance(getIntent().getIntExtra("type", -1), title, null));
        } else if(key == KEY.TYPE_GENE_ELE){
            String ele = getIntent().getStringExtra("ele");
            title = ele;
            openFragment(ListItemFragment.newInstance(KEY.TYPE_GENE, null, ele));
        }else if (key == KEY.IMAGE) {
            ZoomImageView imageView = new ZoomImageView(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setOnTouchListener(new TouchListener(imageView));
            ViewGroupUtils.replaceView(frameLayout, imageView);
            String url = getIntent().getStringExtra("url").replace("square", "large").replace("small", "large");
            String pattern = ".*sinaimg.cn/large/(.*)";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(url);
            if (m.find()) {
                getSupportActionBar().setTitle(m.group(1));
                getSupportActionBar().setSubtitle(url.replace(m.group(1), ""));
            }
            Glide.with(this).load(url).into(imageView);
        } else if (key == KEY.TROPHYTIPS) {
            title = getIntent().getStringExtra("trophy_id");
            Fragment f = new TrophyTipsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("trophy_id", title);
            f.setArguments(bundle);
            openFragment(f);
        } else if (key == KEY.TROPHY) {
            Fragment f = new TrophyFragment();
            String game_id = getIntent().getStringExtra("game_id");
            title = game_id;
            String username = getIntent().getStringExtra("username");
            if (username != null) {
                title += "-" + username;
            }
            Bundle bundle = new Bundle();
            bundle.putString("game_id", game_id);
            bundle.putString("username", username);
            f.setArguments(bundle);
            openFragment(f);
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        KEY.initSetting(this);
    }

    private void openFragment(Fragment f) {
        getSupportActionBar().setTitle(title);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.framelayout, f)
                .commit();
    }

}
