package club.ranleng.psnine.common.multitype.model;

import android.support.annotation.NonNull;

import java.util.Map;


public class ArticleHeader {

    private String content;
    private String username;
    private String icon;
    private String time;
    private String replies;
    private String original;
    private Boolean editable;
    private String title;

    public ArticleHeader() {

    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReplies() {
        return replies;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}