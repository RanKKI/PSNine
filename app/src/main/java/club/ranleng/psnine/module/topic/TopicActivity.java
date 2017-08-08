package club.ranleng.psnine.module.topic;

import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;

public class TopicActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    TopicContract.Presenter mPresenter;

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
        TopicFragment fragment = TopicFragment.newInstance(type, topic_id);
        mPresenter = new TopicPresenter(fragment);
        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout,fragment )
                .commit();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            mPresenter.hidePanel();
            return true;

        }
        return super.dispatchKeyEvent(event);
    }
}
