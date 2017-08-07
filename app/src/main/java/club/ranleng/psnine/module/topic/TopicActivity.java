package club.ranleng.psnine.module.topic;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;

public class TopicActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public void setContentView() {
        setContentView(R.layout.view_toolbar_frame);
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public void getData() {
        int type = getIntent().getIntExtra("type", -1);
        int topic_id = getIntent().getIntExtra("topic_id", -1);
        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, TopicFragment.newInstance(type, topic_id))
                .commit();
    }
}
