package club.ranleng.psnine.widget.HTML;

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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class HtmlImageGetter implements Html.ImageGetter {

    private Context context;
    private TextView tv;

    public HtmlImageGetter(Context context) {
        this.context = context;
    }

    @Override
    public Drawable getDrawable(String s) {
        final URLDrawable urlDrawable = new URLDrawable();
        Glide.with(context).load(s).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int width = resource.getWidth();
                int height = resource.getHeight();
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                int w_screen = dm.widthPixels;
                int h_screen = dm.heightPixels;
                float ratio = 1;
                //max length each line 24
                if (width > w_screen / 3 * 2 || height > h_screen / 3 * 2) {
                    ratio = (float) w_screen / width;
                }
                if (width <= 50 || height <= 50) {
                    ratio = 2;
                }
                float scaleWidth = ((float) width * ratio) / width;
                float scaleHeight = ((float) height * ratio) / height;
                // 取得想要缩放的matrix参数
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // 得到新的图片
                resource = Bitmap.createBitmap(resource, 0, 0, width, height, matrix, true);
                width = resource.getWidth();
                height = resource.getHeight();
                urlDrawable.bitmap = resource;
                urlDrawable.setBounds(0, 0, width, height);

            }
        });
        return urlDrawable;
    }

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
