package club.ranleng.psnine.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;

import com.alexvasilkov.gestures.GestureController;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.common.onRequestPermissionCallback;
import club.ranleng.psnine.utils.Parse;

public class ImageViewActivity extends BaseActivity implements DialogInterface.OnClickListener,
        onRequestPermissionCallback {

    @BindView(R.id.gestureImageView) GestureImageView gestureImageView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    private AlertDialog alertDialog;
    private String url;
    private Context context;
    private List<String> list = new ArrayList<>();

    @Override
    public void setContentView() {
        url = getIntent().getStringExtra("url");
        if (url == null) {
            ToastUtils.showShort(R.string.error);
            finish();
            return;
        }
        if (!url.startsWith("http")) {
            url = "https://ww4.sinaimg.cn/large/" + url;
        }
        setContentView(R.layout.activity_image_view);
        context = this;
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        swipeRefreshLayout.setEnabled(false);
        GestureController controller = gestureImageView.getController();
        controller.setLongPressEnabled(true);
        controller.setOnGesturesListener(new GestureController.SimpleOnGestureListener() {
            public boolean onSingleTapConfirmed(@NonNull MotionEvent event) {
                finish();
                return true;
            }

            public void onLongPress(@NonNull MotionEvent event) {
                alertDialog.show();
            }
        });
    }

    @Override
    public void getData() {
        swipeRefreshLayout.setRefreshing(true);
        if (url.contains("gif") && !url.contains("large")) {
            url = Parse.parseImageUrl(url, 0);
        }
        list.clear();
        list.add(getString(R.string.save));
        if (!url.contains("large")) {
            list.add(getString(R.string.view_original_image));
        }
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.option)
                .setItems(list.toArray(new String[list.size()]), this).create();
        LogUtils.d("Load image: " + url);
        if (url.contains("gif")) {
            Glide.with(this).asGif().load(url).into(new ImageViewTarget<GifDrawable>());
        } else {
            Glide.with(this).load(url).into(new ImageViewTarget<>());
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == 0) {
            if (url.contains("gif")) {
                ToastUtils.showShort("暂不支持GIF.");
                return;
            }
            requestRuntimePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this);
        } else if (!url.contains("large") && which == list.size() - 1) {
            url = Parse.parseImageUrl(url, 0);
            getData();
        }
    }

    @Override
    public void onGranted() {
        Glide.with(context).load(url).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                String path = String.format("%s/Android/data/%s/",
                        Environment.getExternalStorageDirectory(), AppUtils.getAppPackageName());
                String filename = TimeUtils.getNowString() + ".png";
                boolean ok = ImageUtils.save(ImageUtils.drawable2Bitmap(resource),
                        path + filename, Bitmap.CompressFormat.PNG);
                if (ok) {
                    ToastUtils.showShort(R.string.success);
                } else {
                    ToastUtils.showShort(R.string.error);
                }
            }
        });
    }

    @Override
    public void onDenied() {
        ToastUtils.showShort(R.string.permissionDenied);
    }

    class ImageViewTarget<T extends Drawable> extends SimpleTarget<T> {

        @Override
        public void onResourceReady(T resource, Transition<? super T> transition) {
            gestureImageView.setImageDrawable(resource);
            if (resource instanceof GifDrawable) {
                ((GifDrawable) resource).start();
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
