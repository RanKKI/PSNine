package club.ranleng.psnine.model.PSNGame;

import java.util.Map;

/**
 * Created by ran on 09/07/2017.
 */

public class PSNGameHeader {

    public final String icon;
    public final String name;

    public PSNGameHeader(Map<String, Object> map){
        this.icon = (String) map.get("header_icon");
        this.name = (String) map.get("header_name");
    }
}
