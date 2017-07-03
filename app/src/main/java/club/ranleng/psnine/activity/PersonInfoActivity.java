package club.ranleng.psnine.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.Listener.RequestGetListener;
import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.ArticleGameListAdapter;
import club.ranleng.psnine.adapter.ArticleHeaderAdapter;
import club.ranleng.psnine.adapter.ArticleReplyAdapter;
import club.ranleng.psnine.adapter.PSNGameListAdapter;
import club.ranleng.psnine.adapter.TextItemAdapter;
import club.ranleng.psnine.adapter.ViewPagerAdapter.MainPagerAdapter;
import club.ranleng.psnine.adapter.ViewPagerAdapter.PSNPagerAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.fragments.ArticleListFragment;
import club.ranleng.psnine.fragments.PSNFragment;
import club.ranleng.psnine.model.ArticleGameList;
import club.ranleng.psnine.model.ArticleHeader;
import club.ranleng.psnine.model.ArticleReply;
import club.ranleng.psnine.model.GameList;
import club.ranleng.psnine.model.TextItem;
import club.ranleng.psnine.util.DrawableToBitmap;
import club.ranleng.psnine.util.FastBlurUtil;
import club.ranleng.psnine.util.LogUtil;
import club.ranleng.psnine.widget.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestGet;
import club.ranleng.psnine.widget.Requests.RequestPost;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import club.ranleng.psnine.widget.UserStatus;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import okhttp3.FormBody;

public class PersonInfoActivity extends BaseActivity implements FloatingActionButton.OnClickListener, PSNFragment.FinishLoadListener{

    @BindView(R.id.personal_bg) ImageView bg;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.tabs) TabLayout tabs;
    private RequestGet requestGet;
    private String psnid;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_person_info);
    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        fab.setOnClickListener(this);
        requestGet = new RequestGet();
    }

    @Override
    public void getData() {
        psnid = getIntent().getStringExtra("psnid");
        setTitle(psnid.toUpperCase());
        // Load personal background image

    }

    @Override
    public void showContent() {
        List<Fragment> fl = new ArrayList<>(); //填充要的Fragment頁卡
        fl.add(add("psngame"));
        fl.add(add("msg"));
        fl.add(add("topic"));
        fl.add(add("gene"));
        if (viewpager != null) {
            viewpager.setAdapter(new PSNPagerAdapter(getSupportFragmentManager(), fl));  //設定Adapter給viewPager
        }
        tabs.setupWithViewPager(viewpager); //绑定viewPager
    }

    private Fragment add(String i) {
        Fragment f = new PSNFragment();
        Bundle bundle = new Bundle();
        bundle.putString("psnid", psnid);
        bundle.putString("type", i);
        f.setArguments(bundle);
        return f;
    }

    //Fab click action
    @Override
    public void onClick(View v) {
        String[] list = {"关注","感谢","等级同步","游戏同步"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LogUtil.d(which);
                switch (which){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        requestGet.execute("upbase",psnid);
                        break;
                    case 3:
                        requestGet.execute("upgame",psnid);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onFinish(Map<String, Object> results) {
        Glide.with(this).load(results.get("bgurl"))
                .asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int scaleRatio = 4;
                int blurRadius = 30;
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(resource,
                        resource.getWidth() / scaleRatio,
                        resource.getHeight() / scaleRatio,
                        false);
                Bitmap blurBitmap = FastBlurUtil.doBlur(scaledBitmap, blurRadius, true);
                bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                bg.setImageBitmap(blurBitmap);
            }
        });

        Glide.with(this).load(results.get("icon"))
                .asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                fab.setImageBitmap(resource);
            }
        });
    }
}
