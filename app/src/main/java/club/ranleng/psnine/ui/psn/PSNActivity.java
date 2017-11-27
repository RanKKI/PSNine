package club.ranleng.psnine.ui.psn;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.model.PSNUserInfo;
import io.reactivex.functions.Consumer;

public class PSNActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.appbar) AppBarLayout appBarLayout;
    @BindView(R.id.toolbarViews) View toolbarViews;
    @BindView(R.id.PSNAvatar) ImageView avatar;
    @BindView(R.id.PSNRegionIcon) ImageView regionIcon;
    @BindView(R.id.PSNAuthIcon) ImageView authIcon;
    @BindView(R.id.PSNPlusIcon) ImageView plusIcon;
    @BindView(R.id.PSNTrophy) TextView trophy;
    @BindView(R.id.PSNDes) TextView des;

    private float AppbarMaxOffset;
    private Context context;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_psn);
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            AppbarMaxOffset = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                toolbarViews.setAlpha(1 - (1 - verticalOffset) / AppbarMaxOffset);
            }
        });
        context = this;
    }

    @Override
    public void getData() {
        String psnID = getIntent().getStringExtra("psnid");
        setTitle(psnID);
        PSNInfoViewPagerAdapter adapter = new PSNInfoViewPagerAdapter(psnID, getFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        ApiManager.getDefault().getPSN(psnID, Key.PSN, PSNUserInfo.class)
                .subscribe(new Consumer<PSNUserInfo>() {
                    @Override
                    public void accept(PSNUserInfo psnUserInfo) throws Exception {
                        trophy.setText(psnUserInfo.getTrophy());
                        des.setText(psnUserInfo.getDes());
                        Glide.with(context).load(psnUserInfo.getAvatar()).into(avatar);
                        Glide.with(context).load(psnUserInfo.getAuth()).into(authIcon);
                        Glide.with(context).load(psnUserInfo.getPlus()).into(plusIcon);
                        Glide.with(context).load(psnUserInfo.getRegion()).into(regionIcon);

                    }
                });
    }
}
