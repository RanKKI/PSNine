package club.ranleng.psnine.model;

import java.util.Map;

public class GameList {
    public final String game_icon;
    public final String game_name;
    public final String last_time;
    public final String difficulty;
    public final String perfection;
    public final String trophy;
    public final int progress;
    public final String id;


    public GameList(Map<String, Object> map) {

        String a = "<font color='#82ADE3'>";
        String b = "<font color='#CD9A46'>";
        String c = "<font color='#B2BDC3'>";
        String d = "<font color='#BF6F3A'>";

        game_icon = (String) map.get("game_icon");
        progress = (int) map.get("progress");
        game_name = (String) map.get("game_name");
        last_time = (String) map.get("spent_time");
        difficulty = (String) map.get("difficulty");
        perfection = (String) map.get("perfection");
        this.id = (String) map.get("trophy_id");
        trophy = ((String) map.get("trophy"))
                .replace("<span class=\"text-platinum\">", a)
                .replace("<span class=\"text-gold\">", b)
                .replace("<span class=\"text-silver\">", c)
                .replace("<span class=\"text-bronze\">", d)
                .replace("</span>","</font>");
    }
}
