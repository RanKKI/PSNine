package club.ranleng.psnine.module.login;

import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;

public class LoginActivity extends BaseActivity {

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
                .replace(R.id.frameLayout, LoginFragment.newInstance())
                .commit();
    }
}
