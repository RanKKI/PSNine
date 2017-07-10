package club.ranleng.psnine.util;

import android.widget.Toast;

import club.ranleng.psnine.util.AndroidUtilCode.Utils;

/**
 * Created by ran on 03/07/2017.
 */

public class MakeToast {

    public static void str(String s){
        Toast.makeText(Utils.getContext(), s, Toast.LENGTH_SHORT).show();
    }

    public static void plzlogin(){
        str("请先登陆");
    }

    public static void notfound(){
        str("404 - Not Found");
    }
}
