package club.ranleng.psnine.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

import club.ranleng.psnine.model.Common.Image;
import club.ranleng.psnine.util.AndroidUtilCode.LogUtils;

/**
 * Created by ran on 13/07/2017.
 */

public class GlideInToImageView {

    public GlideInToImageView(Context context, final String url, final ImageView imageView){
        imageView.setTag(url);
        Glide.with(context).load(url).crossFade(1000).thumbnail(0.1f).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                imageView.getLayoutParams().height = resource.getIntrinsicHeight();
                if(imageView.getTag().equals(url)){
                    imageView.setImageDrawable(resource);
                }
            }
        });
    }
}
