package club.ranleng.psnine.widget;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ran on 22/06/2017.
 * 用户状态.
 * 是否登陆, 签到
 */

public class UserStatus {

    private static boolean isLogin = false;

    public static boolean isLogin(){
        return isLogin;
    }

    public static void isLogin(Boolean b){
        isLogin = b;
    }

    private static boolean isSignin = false;

    public static boolean isSignin(){
        return isSignin;
    }

    public static void isSignin(Boolean b){
        isSignin = b;
    }


    private static String username = "";
    private static String user_icon_url = "";

    public static String getusername(){
        return username;
    }

    public static String getusericonurl(){
        return user_icon_url;
    }

    public static void setusername(String name,String usericon){
        username = name;
        user_icon_url = usericon;
    }

    public static void Check(String result){
        if(result.contains("个人主页")){
            isLogin(true);
            Document doc = Jsoup.parse(result);
            String pattern = "<li><a href=\"http://psnine.com/psnid/(.*)\">个人主页</a>";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(result);
            String url = doc.select("img.ava").attr("src");
            if (m.find( )) {
                setusername(m.group().replace("\">个人主页</a>","").replace("<li><a href=\"http://psnine.com/psnid/",""),url);
            }
        }
    }

}
