package club.ranleng.psnine.common;

import club.ranleng.psnine.model.UserInfo;

public class UserState {

    private static boolean Login = false;

    public static boolean isLogin() {
        return Login;
    }

    public static void setLogin(boolean login) {
        Login = login;
    }

    private static UserInfo userInfo;

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        UserState.userInfo = userInfo;
    }
}
