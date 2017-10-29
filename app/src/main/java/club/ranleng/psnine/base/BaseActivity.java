package club.ranleng.psnine.base;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.blankj.utilcode.util.Utils;

import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.common.onRequestPermissionCallback;

public abstract class BaseActivity extends AppCompatActivity {

    public onRequestPermissionCallback PermissionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Utils.getApp();
        } catch (NullPointerException e) {
            Utils.init(this.getApplication());
        }
        init();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        setContentView();
        find_setup_Views();
        getData();
    }

    public abstract void setContentView();

    public abstract void find_setup_Views();

    public abstract void getData();

    @TargetApi(Build.VERSION_CODES.M)
    public void requestRuntimePermission(String permission, onRequestPermissionCallback PermissionCallback) {
        this.PermissionCallback = PermissionCallback;
        switch (checkSelfPermission(permission)) {
            case PackageManager.PERMISSION_GRANTED:
                if (this.PermissionCallback != null) {
                    PermissionCallback.onGranted();
                }
                break;
            case PackageManager.PERMISSION_DENIED:
                requestPermissions(new String[]{permission}, Key.REQUEST_PERMISSION);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Key.REQUEST_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (PermissionCallback != null) {
                    PermissionCallback.onGranted();
                }
            } else {
                if (PermissionCallback != null) {
                    PermissionCallback.onDenied();
                }
            }
        }
    }

}
