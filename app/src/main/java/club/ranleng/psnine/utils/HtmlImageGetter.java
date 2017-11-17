package club.ranleng.psnine.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import io.reactivex.annotations.Nullable;

public class HtmlImageGetter implements Html.ImageGetter {

    private Context context;
    private TextView tv;
    private int w_screen;
    private int h_screen;

    public HtmlImageGetter(Context context, @Nullable TextView tv) {
        this.context = context;
        this.tv = tv;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        w_screen = dm.widthPixels;
        h_screen = dm.heightPixels;
    }

    @Override
    public Drawable getDrawable(final String s) {
        final URLDrawable urlDrawable = new URLDrawable();
        int w = 64;
        int h = 64;
        if (!s.contains("photo.psnine.com/face")) {
            ArrayList<Integer> list = LCache.get(s);
            if (list != null) {
                w = list.get(0);
                h = list.get(1);
            }
        }
        urlDrawable.setBounds(0, 0, w, h);
        Glide.with(context).asBitmap().thumbnail(0.5f).load(s)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        int width = resource.getWidth();
                        int height = resource.getHeight();
                        float ratio = 1;
                        //max length each line 24
                        if (width > w_screen / 3 * 2 || height > h_screen / 3 * 2) {
                            ratio = (float) w_screen / 3 * 2 / width;
                        }
                        if (width <= 50 || height <= 50) {
                            ratio = 2;
                        }
                        float scaleWidth = ((float) width * ratio) / width;
                        float scaleHeight = ((float) height * ratio) / height;
                        // 取得想要缩放的 matrix 参数
                        Matrix matrix = new Matrix();
                        matrix.postScale(scaleWidth, scaleHeight);
                        // 得到新的图片
                        resource = Bitmap.createBitmap(resource, 0, 0, width, height, matrix, true);
                        width = resource.getWidth();
                        height = resource.getHeight();
                        LCache.add(s, width, height);
                        urlDrawable.bitmap = resource;
                        urlDrawable.setBounds(0, 0, width, height);
                        if (tv != null) {
                            tv.invalidate();
                            tv.setText(tv.getText()); // 解决图文重叠
                        }
                    }
                });
        return urlDrawable;
    }

    @SuppressWarnings("deprecation")
    private class URLDrawable extends BitmapDrawable {

        Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }
}