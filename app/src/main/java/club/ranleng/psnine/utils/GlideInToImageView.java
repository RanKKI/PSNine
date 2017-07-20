package club.ranleng.psnine.utils;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import club.ranleng.psnine.R;


public class GlideInToImageView {

    public GlideInToImageView(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.cache)
                .error(android.R.drawable.stat_notify_error)
                .crossFade()
                .into(imageView);
    }
}
