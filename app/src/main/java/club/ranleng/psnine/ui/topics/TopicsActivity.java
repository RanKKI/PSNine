package club.ranleng.psnine.ui.topics;

import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.common.KeyGetter;

public class TopicsActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_toolbar_framelayout);
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public void getData() {
        int type = getIntent().getIntExtra("type", -1);
        if (type == -1) {
            finish();
            return;
        }
        String query = getIntent().getStringExtra("query");
        String title = KeyGetter.getKEYName(type);
        if (query == null) {
            query = "";
        } else {
            title = query;
        }
        setTitle(title);
        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, TopicsFragment.newInstance(type, query))
                .commit();
    }
}
