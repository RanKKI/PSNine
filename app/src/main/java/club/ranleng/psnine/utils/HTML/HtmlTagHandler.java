package club.ranleng.psnine.utils.HTML;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;

import org.xml.sax.Attributes;

import java.util.Locale;

import club.ranleng.psnine.module.photo.ViewImageActivity;


public class HtmlTagHandler implements mHtml.TagHandler {

    private Context ctx;

    public HtmlTagHandler(Context context) {
        this.ctx = context.getApplicationContext();
    }


    @Override
    public void handleTag(boolean opening, String tag, Editable output, Attributes attributes) {
        if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
            // 获取长度
            int len = output.length();
            // 获取图片地址
            ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
            String imgURL = images[0].getSource();

            // 使图片可点击并监听点击事件
            if (!imgURL.contains("http://photo.psnine.com/face")) {
                output.setSpan(new ClickableImage(ctx, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }

    }


    public class ClickableImage extends ClickableSpan {

        private String url;
        private Context context;

        public ClickableImage(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(context, ViewImageActivity.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }
}
