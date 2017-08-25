package club.ranleng.psnine.module.newtopic;

import android.app.Fragment;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.module.newtopic.Gene.newGeneFragment;
import club.ranleng.psnine.module.newtopic.Topic.newTopicFragment;

public class NewTopicActivity extends BaseActivity {

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
        boolean edit = getIntent().getBooleanExtra("edit", false);
        String topic_id = getIntent().getStringExtra("topic_id");
        if (type == -1) {
            finish();
            return;
        }
        Fragment fragment;
        if (type == KEY.GENE) {
            fragment = newGeneFragment.newInstance(edit, topic_id);
        } else {
            fragment = newTopicFragment.newInstance(edit, topic_id);
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}
