package club.ranleng.psnine.common.multitype.model;

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Created by ran on 01/07/2017.
 */

public class ArticleReply {

    private Boolean editable;
    private String comment_id;
    private Object icon;
    private String username;
    private String title;
    private String time;

    public ArticleReply(@NonNull Map<String, Object> map) {
        this.title = (String) map.get("title");
        this.comment_id = (String) map.get("id");
        this.editable = (Boolean) map.get("editable");
        this.username = (String) map.get("username");
        this.time = (String) map.get("time");
        this.icon = map.get("icon");
    }

    @Override
    public String toString() {
        return "ArticleReply{" +
                "editable=" + editable +
                ", comment_id='" + comment_id + '\'' +
                ", icon=" + icon +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
