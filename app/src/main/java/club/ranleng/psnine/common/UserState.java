package club.ranleng.psnine.common;

import com.blankj.utilcode.util.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import club.ranleng.psnine.common.event.UserInfoLoad;


public class UserState {

    private static boolean isLogin = false;
    private static String username = null;
    private static String userIcon = null;
    private static boolean dao = true;
    private static boolean notice = false;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserState.username = username;
    }

    public static boolean isLogin() {
        return isLogin;
    }

    public static boolean getDao() {
        return dao;
    }

    public static void setDao(boolean dao) {
        UserState.dao = dao;
    }

    public static String getUserIcon() {
        return userIcon.replace("@50w.png","");
    }

    public static void setUserIcon(String userIcon) {
        UserState.userIcon = userIcon;
    }

    public static boolean getNotice() {
        return notice;
    }

    public static void setNotice(boolean notice) {
        UserState.notice = notice;
    }

    public static void setIsLogin(boolean isLogin) {
        UserState.isLogin = isLogin;
    }

    public static void Check(String result) {

        if (result == null) {
            return;
        }

        if(isLogin()){
            return;
        }

        if (result.contains("个人主页")) {
            setIsLogin(true);

            Document doc = Jsoup.parse(result);

            if (doc.select("a.yuan").size() == 2) {
                setDao(false);
            }

            if (doc.select("span.badge.dot").size() != 0) {
                setNotice(true);
            }

            setUserIcon(doc.select("img.ava").attr("src"));

            String pattern = "<li><a href=\"http://psnine.com/psnid/(.*)\">个人主页</a>";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(result);

            if (m.find()) {
                setUsername(m.group().replace("\">个人主页</a>", "")
                        .replace("<li><a href=\"http://psnine.com/psnid/", ""));
            }

            UserInfoLoad load = new UserInfoLoad();
            load.setName(getUsername());
            load.setIcon(getUserIcon());
            load.setDao(getDao());
            load.setMsg(getNotice());
            RxBus.getDefault().send(load);
        }
    }
}
