package club.ranleng.psnine.model;

import java.util.Map;

/**
 * Created by ran on 09/07/2017.
 */

public class PSNGameUser {

    public final String username;
    public final String percentage;
    public final String ft;
    public final String lt;
    public final String tt;

    public PSNGameUser(Map<String, Object> map) {
        this.username = (String) map.get("username");
        this.percentage = (String) map.get("percentage");
        this.ft = (String) map.get("first_trophy");
        this.lt = (String) map.get("last_trophy");
        this.tt = (String) map.get("total_time");
    }
}
