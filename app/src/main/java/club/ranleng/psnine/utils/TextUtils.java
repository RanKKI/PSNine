package club.ranleng.psnine.utils;

import android.widget.EditText;

public class TextUtils {

    public static String toS(EditText editText){
        return editText.getText().toString();
    }

    public static boolean isEmpty(EditText editText){
        return editText.getText().toString().isEmpty();
    }
}