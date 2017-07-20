package club.ranleng.psnine.model.Article;

import java.util.Map;

/**
 * Created by ran on 06/07/2017.
 */

public class ArticleTrophy {

    public final String game_icon_url;
    public final String game_name;
    public final String game_des;
    public final String user_name;
    public final String user_comment;
    public final String time;
    public final String has_comment;
    public final String trophy_id;

    public ArticleTrophy(Map<String, String> map) {
        this.game_icon_url = map.get("game_icon_url");
        this.game_name = map.get("game_name");
        this.game_des = map.get("game_des");
        this.user_name = map.get("user_name");
        this.user_comment = map.get("user_comment");
        this.time = map.get("time");
        this.has_comment = map.get("has_comment");
        this.trophy_id = map.get("trophy_id");
    }


}
