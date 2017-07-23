package club.ranleng.psnine.widget;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ran .
 */

public class UserStatus {

    private static boolean isLogin = false;
    private static String username = "";
    private static String user_icon_url = "";
    private static Boolean dao = true;
    private static Boolean notice = false;

    public static boolean isLogin() {
        return isLogin;
    }

    private static void isLogin(Boolean b) {
        isLogin = b;
    }

    public static String getusername() {
        return username;
    }

    public static String getusericonurl() {
        return user_icon_url;
    }

    private static void setusername(String name, String usericon) {
        username = name;
        user_icon_url = usericon;
    }

    public static Boolean getdao() {
        return dao;
    }

    public static void setdao(Boolean d) {
        dao = d;
    }

    public static void setNotice(Boolean a){
        notice = a;
    }
    public static Boolean getNotice(){
        return notice;
    }

    public static Boolean Check(String result) {
        if (result == null) {
            return false;
        }
        if (result.contains("个人主页")) {
            isLogin(true);
            Document doc = Jsoup.parse(result);
            if (doc.select("a.yuan").size() == 2) {
                setdao(false);
            }
            if(doc.select("span.badge.dot").size() != 0){
                setNotice(true);
            }
            String pattern = "<li><a href=\"http://psnine.com/psnid/(.*)\">个人主页</a>";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(result);
            String url = doc.select("img.ava").attr("src");
            if (m.find()) {
                setusername(m.group().replace("\">个人主页</a>", "").replace("<li><a href=\"http://psnine.com/psnid/", ""), url);
            }
            return true;
        }else{
            isLogin(false);
        }
        return false;
    }
}
