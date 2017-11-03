package club.ranleng.psnine.ui.setting;

import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public void getData() {
        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, SettingFragment.newInstance())
                .commit();
    }
}

