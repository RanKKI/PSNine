package club.ranleng.psnine.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class UserInfo {

    @Pick(value = "img.ava", attr = Attrs.SRC)
    private String avatar;
    @Pick(value = "ul.r > li.dropdown > ul > li:first-child > a", attr = Attrs.HREF)
    private String username;
    @Pick(value = "a.yuan:not(.mt10)")
    private String sign;
    @Pick(value = "span.badge.dot")
    private String notice;

    public String getAvatar() {
        if (!avatar.contains("?")) {
            return avatar;
        }
        String pattern = "(.*)\\?(.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(avatar);
        if (m.find()) {
            avatar = m.group(1);
        }
        return avatar;
    }

    public String getUsername() {
        if (username != null && username.startsWith("http://psnine.com/psnid/")) {
            username = username.replace("http://psnine.com/psnid/", "");
        }
        return username;
    }

    public Boolean getSign() {
        return sign == null;
    }

    public Boolean getNotice() {
        return notice == null;
    }
}
