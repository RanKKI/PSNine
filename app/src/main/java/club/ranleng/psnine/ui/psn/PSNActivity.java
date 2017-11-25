package club.ranleng.psnine.ui.psn;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;

public class PSNActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.appbar) AppBarLayout appBarLayout;
    @BindView(R.id.toolbarViews) View toolbarViews;

    private float AppbarMaxOffset;
    private String psnID;

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
    }

    @Override
    public void getData() {
        psnID = getIntent().getStringExtra("psnid");
        PSNInfoViewPagerAdapter adapter = new PSNInfoViewPagerAdapter(psnID, getFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
