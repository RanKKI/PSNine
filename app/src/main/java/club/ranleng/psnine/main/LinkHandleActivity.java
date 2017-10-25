package club.ranleng.psnine.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;

public class LinkHandleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        LogUtils.d(action);
        LogUtils.d(data);
    }
}
