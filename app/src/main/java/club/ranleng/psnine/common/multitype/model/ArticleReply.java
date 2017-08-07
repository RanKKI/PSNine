package club.ranleng.psnine.common.multitype.model;

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Created by ran on 01/07/2017.
 */

public class ArticleReply {

    public final Boolean editable;
    public final String comment_id;
    public final Object icon;
    public final String username;
    public final String title;
    public final String time;

    public ArticleReply(@NonNull Map<String, Object> map) {
        this.title = (String) map.get("title");
        this.comment_id = (String) map.get("id");
        this.editable = (Boolean) map.get("editable");
        this.username = (String) map.get("username");
        this.time = (String) map.get("time");
        this.icon = map.get("icon");
    }
}
