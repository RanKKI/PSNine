package club.ranleng.psnine.module.photo;

import android.support.v7.widget.Toolbar;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;

public class ViewImageActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.photoview) PhotoView photoView;

    private String large_url;
    private String key;

    @Override
    public void setContentView() {
        String url = getIntent().getStringExtra("url");
        if (url == null) {
            finish();
            return;
        }
        if (!url.contains(".jpg")) {
            url += ".jpg";
        }
        Pattern r = Pattern.compile(".*?sinaimg\\.cn/.*?/(.*?)\\.jpg");
        Matcher m = r.matcher(url);
        if (m.find()) {
            key = m.group(1);
            large_url = String.format("http://wx4.sinaimg.cn/large/%s.jpg", key);
        }
        setContentView(R.layout.activity_view_image);
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(large_url);
        LogUtils.d("load image " + large_url);
    }

    @Override
    public void getData() {
        photoView.enable();
        photoView.animaFrom(photoView.getInfo());
        if (large_url == null) {
            ToastUtils.showShort("cannot resolve URL");
            finish();
            return;
        }
        Glide.with(this)
                .load(large_url)
                .into(photoView);
    }
}
