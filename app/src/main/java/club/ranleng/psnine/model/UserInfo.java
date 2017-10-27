package club.ranleng.psnine.model;

import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class UserInfo {

    @Pick(value = "img.ava", attr = Attrs.SRC)
    private String avatar;
    @Pick(value = "ul.r > li.dropdown > ul > li:first-child > a", attr = Attrs.HREF)
    private String username;

    public String getAvatar() {
        if (avatar != null && avatar.contains("?x-oss-process=image/resize,w_50")){
            avatar = avatar.replace("?x-oss-process=image/resize,w_50", "");
        }
        return avatar;
    }

    public String getUsername() {
        if (username != null && username.startsWith("http://psnine.com/psnid/")) {
            return username.replace("http://psnine.com/psnid/", "");
        }
        return username;
    }
}
