package club.ranleng.psnine.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.ActivityUtils;

import club.ranleng.psnine.ui.topic.TopicActivity;

public class LinkHandleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            String fullUrl = data.toString();
            String path = fullUrl.replace("http://psnine.com/", "");
            if (path.startsWith("topic") || path.startsWith("gene") || path.startsWith("qa")) {
                Bundle bundle = new Bundle();
                bundle.putString("url", fullUrl);
                ActivityUtils.startActivity(bundle, TopicActivity.class);
            }
        }
        finish();
    }
}
