package club.ranleng.psnine.utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import club.ranleng.psnine.widget.KEY;

public class TextUtils {

    public static String toS(EditText editText){
        return editText.getText().toString();
    }

    public static boolean isEmpty(EditText editText){
        return editText.getText().toString().isEmpty();
    }

    public static String editReply(String content){
        content = content.replace("\n", "").replace("\r", "");
        content = content.replace("&nbsp;", " ").replace("<br>", "\n");
        String pattern = "<a href=\"http://psnine.com/psnid/.*\">(@.*)</a>";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(content);

        while (m.find()) {
            content = content.replace(m.group(0), m.group(1).replace("\n", "").replace("\r", ""));
        }

        pattern = "<img src=\"http://photo.psnine.com/face/(.*?).gif\">";
        r = Pattern.compile(pattern);
        m = r.matcher(content);
        while (m.find()) {
            content = content.replace(m.group(0), KEY.EMOJI_URL_STR.get(m.group(1)));
        }
        return content;
    }

}
