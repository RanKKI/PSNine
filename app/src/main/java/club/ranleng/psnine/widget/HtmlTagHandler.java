package club.ranleng.psnine.widget;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;

import org.xml.sax.XMLReader;

import java.util.Locale;

import club.ranleng.psnine.activity.Assist.ImageActivity;
import club.ranleng.psnine.util.LogUtils;

/**
 * Created by ran on 02/07/2017.
 */

public class HtmlTagHandler implements TagHandler {

    private Context ctx;

    public HtmlTagHandler(Context context){
         this.ctx = context;
    }
    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
            // 获取长度
            int len = output.length();
            // 获取图片地址
            ImageSpan[] images = output.getSpans(len-1, len, ImageSpan.class);
            String imgURL = images[0].getSource();

            // 使图片可点击并监听点击事件
            if(!imgURL.contains("http://photo.psnine.com/face")){
                output.setSpan(new ClickableImage(ctx, imgURL), len-1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else if (tag.toLowerCase(Locale.getDefault()).equals("iframe")){
            int len = output.length();
            LogUtils.d(len);
        }
    }

    private class ClickableImage extends ClickableSpan {

        private String url;
        private Context context;

        public ClickableImage(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(context, ImageActivity.class);
            intent.putExtra("url",url);
            context.startActivity(intent);

        }
    }
}
