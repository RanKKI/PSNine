package club.ranleng.psnine.activity.Main;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.ViewPagerAdapter.PSNPagerAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.fragments.PSNFragment;
import club.ranleng.psnine.util.AndroidUtilCode.LogUtils;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestGet;

import static android.R.attr.bitmap;

public class PersonInfoActivity extends BaseActivity implements FloatingActionButton.OnClickListener, PSNFragment.FinishLoadListener{

    @BindView(R.id.personal_bg) ImageView bg;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.tabs) TabLayout tabs;
    private RequestGet requestGet;
    private String psnid;
    private Context context;

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        fab.setOnClickListener(this);
        requestGet = new RequestGet();
        toolbar.setTitleTextColor(getResources().getColor(R.color.cardview_light_background));
    }

    @Override
    public void getData() {
        context = this;
        psnid = getIntent().getStringExtra("psnid");
        if(psnid == null){
            finish();
        }
        setTitle(psnid.toUpperCase());
        // Load personal background image
        showContent();
    }

    public void showContent() {
        List<Fragment> fl = new ArrayList<>(); //填充要的Fragment頁卡
        fl.add(add("psngame"));
        fl.add(add("msg"));
        fl.add(add("topic"));
        fl.add(add("gene"));
        if (viewpager != null) {
            viewpager.setAdapter(new PSNPagerAdapter(getFragmentManager(), fl));  //設定Adapter給viewPager
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
                LogUtils.d(which);
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
                bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                bg.setImageBitmap(resource);
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
