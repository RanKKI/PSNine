package club.ranleng.psnine.activity.Assist;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import club.ranleng.psnine.util.LogUtils;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String scheme = intent.getScheme();
        Uri uri = intent.getData();
        String str = "";
        if (uri != null) {
            String host = uri.getHost();
            String dataString = intent.getDataString();
            String from = uri.getQueryParameter("from");
            String path = uri.getPath();
            String encodedPath = uri.getEncodedPath();
            String queryString = uri.getQuery();
            LogUtils.d(host);
            LogUtils.d(dataString);
            LogUtils.d(from);
            LogUtils.d(path);
            LogUtils.d(encodedPath);
            LogUtils.d(queryString);

        }
    }
}
