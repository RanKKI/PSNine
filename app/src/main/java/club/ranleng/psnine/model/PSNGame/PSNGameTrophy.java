package club.ranleng.psnine.model.PSNGame;

import android.support.annotation.Nullable;

import java.util.Map;

/**
 * Created by ran on 09/07/2017.
 */

public class PSNGameTrophy {

    public final String icon;
    public final String name;
    public final String des;
    public final String percent;
    @Nullable public final String date;
    @Nullable public final String id;

    public PSNGameTrophy(Map<String, Object> map){

        this.icon = (String) map.get("trophy_icon");
        this.id = (String) map.get("trophy_id");
        this.name = ((String) map.get("trophy_name")).replace((String) map.get("trophy_tips"),"").replace("(","\n(").replace("（","\n（");
        this.des = ((String) map.get("trophy_des")).replace((String) map.get("trophy_tips"),"");
        this.date = ((String) map.get("trophy_date")).replace("<br>","\n");
        this.percent = (String) map.get("trophy_percent");
    }
}
