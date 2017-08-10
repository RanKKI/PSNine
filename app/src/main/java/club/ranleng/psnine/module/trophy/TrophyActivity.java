package club.ranleng.psnine.module.trophy;

import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.module.topic.TopicFragment;

public class TrophyActivity extends BaseActivity {

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
        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout,
                        TrophyFragment.newInstance(getIntent().getStringExtra("game_id"),
                        getIntent().getStringExtra("username")))
                .commit();
    }
}
