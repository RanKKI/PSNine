package club.ranleng.psnine.common.multitype.model;

import android.support.annotation.NonNull;

import java.util.Map;


public class ArticleHeader {

    public final String content;
    public final String username;
    public final String icon;
    public final String time;
    public final String replies;
    public final String original;
    public final Boolean editable;
    public final String title;

    public ArticleHeader(@NonNull Map<String, Object> map) {
        this.content = (String) map.get("content");
        this.username = (String) map.get("username");
        this.icon = (String) map.get("icon");
        this.time = (String) map.get("time");
        this.replies = (String) map.get("replies");
        this.editable = (Boolean) map.get("editable");
        this.original = (String) map.get("original");
        this.title = (String) map.get("title");
    }

}