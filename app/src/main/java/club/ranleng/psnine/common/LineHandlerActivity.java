package club.ranleng.psnine.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.Utils;

import club.ranleng.psnine.module.psn.PSNActivity;
import club.ranleng.psnine.module.topic.TopicActivity;

public class LineHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Utils.getContext();
        } catch (NullPointerException e) {
            Utils.init(this.getApplicationContext());
        }

        Uri data = getIntent().getData();
        String dataPath = data.getPath();
        String path = data.getPath().substring(1, dataPath.length());

        Intent intent = null;
        if (path.contains("gene/")) {
            intent = new Intent(this, TopicActivity.class);
            intent.putExtra("type", KEY.GENE);
            intent.putExtra("topic_id", path.replace("gene/", ""));
        } else if (path.contains("topic/")) {
            intent = new Intent(this, TopicActivity.class);
            intent.putExtra("type", KEY.TOPIC);
            intent.putExtra("topic_id", path.replace("topic/", ""));
        } else if (path.contains("qa/")) {
            intent = new Intent(this, TopicActivity.class);
            intent.putExtra("type", KEY.QA);
            intent.putExtra("topic_id", path.replace("qa/", ""));
        } else if (path.contains("qa/")) {
            intent = new Intent(this, TopicActivity.class);
            intent.putExtra("type", KEY.QA);
            intent.putExtra("topic_id", path.replace("qa/", ""));
        } else if (path.contains("psnid/")) {
            intent = new Intent(this, PSNActivity.class);
            intent.putExtra("psnid", path.replace("psnid/", ""));
        }

        if (intent != null) {
            startActivity(intent);
        }
        finish();
    }
}
