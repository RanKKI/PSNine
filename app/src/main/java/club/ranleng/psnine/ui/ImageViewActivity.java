package club.ranleng.psnine.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.alexvasilkov.gestures.GestureController;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;

import club.ranleng.psnine.R;

public class ImageViewActivity extends AppCompatActivity {

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("url");
        if (url == null) {
            ToastUtils.showShort(R.string.error);
            finish();
            return;
        }
        GestureImageView imageView = new GestureImageView(this);
        setContentView(imageView);
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
        Glide.with(this).load(url).into(imageView);
        String[] items = new String[]{"Save Image", "Other"};
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Option")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtils.showShort(String.valueOf(which));
                    }
                }).create();
    }

    private void showDialogMenu() {
        if (alertDialog.isShowing()) {
            return;
        }
        alertDialog.show();
    }
}
