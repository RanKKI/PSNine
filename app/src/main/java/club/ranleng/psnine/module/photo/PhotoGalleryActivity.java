package club.ranleng.psnine.module.photo;

import android.support.v7.widget.Toolbar;

import com.blankj.utilcode.util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;

public class PhotoGalleryActivity extends BaseActivity {

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
                .replace(R.id.frameLayout, PhotoGalleryFragment.newInstance(getIntent().getStringArrayListExtra("photo_list")))
                .commit();
    }
}
