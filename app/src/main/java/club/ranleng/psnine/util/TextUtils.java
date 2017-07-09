package club.ranleng.psnine.util;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ran on 04/07/2017.
 */

public class TextUtils {

    public static String toS(EditText editText){
        return editText.getText().toString();
    }

    public static String toS(TextView textView){
        return textView.getText().toString();
    }

    public static int toI(EditText editText){
        return Integer.valueOf(toS(editText));
    }

}
