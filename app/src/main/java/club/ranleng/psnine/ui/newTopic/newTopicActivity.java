package club.ranleng.psnine.ui.newTopic;

import android.app.Fragment;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.common.Key;

public class newTopicActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    private int type;

    @Override
    public void setContentView() {
        type = getIntent().getIntExtra("type", -1);
        if (type == -1) {
            finish();
            return;
        }
        setContentView(R.layout.activity_toolbar_framelayout);
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public void getData() {
        Fragment fragment;
        if (type == Key.GENE) {
            fragment = newTopicGeneFragment.newInstance();
        } else {
            fragment = newTopicFragment.newInstance(type);
        }
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

}
