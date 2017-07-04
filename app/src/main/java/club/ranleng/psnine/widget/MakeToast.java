package club.ranleng.psnine.widget;

import android.content.Context;
import android.widget.Toast;

import club.ranleng.psnine.util.Utils;

/**
 * Created by ran on 03/07/2017.
 */

public class MakeToast {

    public MakeToast(Context context, String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    public static void plzlogin(){
        Toast.makeText(Utils.getContext(), "请先登陆", Toast.LENGTH_SHORT).show();
    }

    public static void notfound(){
        Toast.makeText(Utils.getContext(), "404 - Not Found", Toast.LENGTH_SHORT).show();
    }
}
