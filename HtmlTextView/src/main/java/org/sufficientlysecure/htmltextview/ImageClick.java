package org.sufficientlysecure.htmltextview;

import android.support.annotation.Nullable;

public class ImageClick {

    @Nullable
    private UrlClickListener urlClickListener;
    @Nullable
    private String patter;

    public ImageClick() {
    }

    public ImageClick(@Nullable UrlClickListener urlClickListener, @Nullable String patter) {
        this.urlClickListener = urlClickListener;
        this.patter = patter;
    }

    @Nullable
    UrlClickListener getUrlClickListener() {
        return urlClickListener;
    }

    public void setUrlClickListener(@Nullable UrlClickListener urlClickListener) {
        this.urlClickListener = urlClickListener;
    }

    @Nullable
    String getPatter() {
        return patter;
    }

    public void setPatter(@Nullable String patter) {
        this.patter = patter;
    }

    public interface UrlClickListener {

        void OnClick(String url);

    }
}
