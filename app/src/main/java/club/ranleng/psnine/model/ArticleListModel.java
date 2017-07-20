package club.ranleng.psnine.model;

import java.util.Map;

/**
 * Created by ran .
 */

public class ArticleListModel {

    public final String icon;
    public final String title;
    public final String username;
    public final String time;
    public final int id;
    public final String reply;
    public final int type;


    public ArticleListModel(Map<String, Object> map) {
        this.icon = (String) map.get("icon");
        this.title = (String) map.get("title");
        this.time = (String) map.get("time");
        this.id = (int) map.get("id");
        this.username = (String) map.get("username");
        this.reply = (String) map.get("reply");
        this.type = (int) map.get("type");
    }
}
