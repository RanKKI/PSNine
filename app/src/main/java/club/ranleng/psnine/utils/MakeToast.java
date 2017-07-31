package club.ranleng.psnine.utils;

import android.app.Activity;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;

public class MakeToast {

    public static void str(String str) {
        Toast.makeText(Utils.getContext(), str, Toast.LENGTH_SHORT).show();
    }

    public static void str(Activity activity,String str) {
        Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
    }

    public static void notfound(){
        str("404 NOT FOUND");
    }

    public static void plzlogin(){ str("请先登陆");}
}
