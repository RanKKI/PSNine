package club.ranleng.psnine.activity.Assist;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;

public class ImageActivity extends BaseActivity {

    @BindView(R.id.imageview) ImageView imageview;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_image);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void getData() {
        Glide.with(this).load(getIntent().getStringExtra("url")).into(imageview);
    }

    @Override
    public void showContent() {

    }
}
