package club.ranleng.psnine.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ran on 02/07/2017.
 */

public class ArticleGameList {

    public final String name;
    public final String mode;
    public final String percent;
    public final String trophy;
    public final String icon;

    public ArticleGameList(@NonNull Map<String, Object> map) {

        String a = "<font color='#82ADE3'>";
        String b = "<font color='#CD9A46'>";
        String c = "<font color='#B2BDC3'>";
        String d = "<font color='#BF6F3A'>";

        this.name = (String) map.get("game_name");
        this.mode = (String) map.get("game_mode");
        this.percent = (String) map.get("game_percent");
        String temp = (String) map.get("game_trophy");
        this.trophy = temp.replace("<em class=\"text-platinum\">", a)
                .replace("<em class=\"text-gold\">", b)
                .replace("<em class=\"text-silver\">", c)
                .replace("<em class=\"text-bronze\">", d)
                .replace("</em>","</font>");

        this.icon = (String) map.get("game_icon");

    }
}
