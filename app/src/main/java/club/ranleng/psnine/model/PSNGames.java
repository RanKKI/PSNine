package club.ranleng.psnine.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ran on 03/07/2017.
 */

public class PSNGames {
    public final String icon;
    public final String name;
    public final String mode;
    public final String perfect;
    public final String trophy;
    public final String id;

    public PSNGames(Map<String, Object> map) {

        String a = "<font color='#82ADE3'>";
        String b = "<font color='#CD9A46'>";
        String c = "<font color='#B2BDC3'>";
        String d = "<font color='#BF6F3A'>";

        icon = (String) map.get("icon");
        name = ((String) map.get("name")).replace("</span>","</span>&nbsp;");
        mode = (String) map.get("mode");
        perfect = (String) map.get("percent");
        this.id = (String) map.get("trophy_id");
        trophy = ((String) map.get("trophy"))
                .replace("<span class=\"text-platinum\">", a)
                .replace("<span class=\"text-gold\">", b)
                .replace("<span class=\"text-silver\">", c)
                .replace("<span class=\"text-bronze\">", d)
                .replace("</span>","</font>");
    }
}
