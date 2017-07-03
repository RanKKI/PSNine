package club.ranleng.psnine.widget;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ran on 03/07/2017.
 */

public class MakeToast {

    public MakeToast(Context context, String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    public static void plzlogin(Context context){
        Toast.makeText(context, "请先登陆", Toast.LENGTH_SHORT).show();
    }

    public static void notfound(Context context){
        Toast.makeText(context, "404 - Not Found", Toast.LENGTH_SHORT).show();
    }
}
