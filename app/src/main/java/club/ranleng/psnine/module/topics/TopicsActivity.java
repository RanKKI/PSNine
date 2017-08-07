package club.ranleng.psnine.module.topics;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.module.topic.TopicFragment;

public class TopicsActivity extends BaseActivity {


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.app_bar) AppBarLayout appbar;

    @Override
    public void setContentView() {
        setContentView(R.layout.view_toolbar_frame);
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        toolbar.setLayoutParams(params);
    }

    @Override
    public void getData() {
        int type = getIntent().getIntExtra("type", -1);
        String query = getIntent().getStringExtra("query");
        if(query != null){
            setTitle(query);
        }
        String ele = getIntent().getStringExtra("ele");
        if(ele != null){
            setTitle(ele);
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, TopicsFragment.newInstance(type, query, ele))
                .commit();
    }
}
