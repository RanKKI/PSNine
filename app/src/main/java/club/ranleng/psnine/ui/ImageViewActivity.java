package club.ranleng.psnine.ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;

import com.alexvasilkov.gestures.GestureController;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.common.onRequestPermissionCallback;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ImageViewActivity extends BaseActivity implements onRequestPermissionCallback, DialogInterface.OnClickListener {

    private AlertDialog alertDialog;
    private GestureImageView imageView;
    private String url;

    @Override
    public void setContentView() {
        url = getIntent().getStringExtra("url");
        if (url == null) {
            ToastUtils.showShort(R.string.error);
            finish();
            return;
        }
        imageView = new GestureImageView(this);
        setContentView(imageView);
    }

    @Override
    public void find_setup_Views() {
        imageView.getController().setLongPressEnabled(true);
        imageView.getController().setOnGesturesListener(new GestureController.SimpleOnGestureListener() {
            public boolean onSingleTapConfirmed(@NonNull MotionEvent event) {
                finish();
                return true;
            }

            public void onLongPress(@NonNull MotionEvent event) {
                showDialogMenu();
            }
        });
    }

    @Override
    public void getData() {
        Glide.with(this).load(url).into(imageView);
        String[] items = new String[]{"Save Image", "Other"};
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.option)
                .setItems(items, this).create();
    }

    private void showDialogMenu() {
        if (alertDialog.isShowing()) {
            return;
        }
        alertDialog.show();
    }

    @Override
    public void onGranted() {
        Glide.with(this).load(url).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                String path = String.format("%s/Android/data/%s/",
                        Environment.getExternalStorageDirectory(), AppUtils.getAppPackageName());
                String filename = TimeUtils.getNowString() + ".png";
                ImageUtils.save(ImageUtils.drawable2Bitmap(resource),
                        path + filename, Bitmap.CompressFormat.PNG);
            }
        });
    }

    @Override
    public void onDenied() {
        ToastUtils.showShort(R.string.permissionDenied);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == 0) {
            requestRuntimePermission(WRITE_EXTERNAL_STORAGE, this);
        }
    }
}
