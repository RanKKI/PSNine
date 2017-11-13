package club.ranleng.psnine.utils;

import android.widget.EditText;

import java.util.List;

public class TextUtils {

    public static String toString(EditText editText) {
        return editText.getText().toString();
    }

    public static String toString(int num) {
        return String.valueOf(num);
    }

    public static String toStringPhoto(List<String> list) {
        StringBuilder p = new StringBuilder();
        for (String i : list) {
            p.append(i).append(",");
        }

        if (!p.toString().contentEquals("")) {
            p = new StringBuilder(p.substring(0, p.length() - 1));
        }
        return p.toString();
    }

}
