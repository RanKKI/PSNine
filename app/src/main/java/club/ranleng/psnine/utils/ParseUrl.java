package club.ranleng.psnine.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import club.ranleng.psnine.common.Key;

public class ParseUrl {

    public static int getType(String url) {
        url = url.replace("http://psnine.com/", "");
        if (url.startsWith("gene")) {
            return Key.GENE;
        } else if (url.startsWith("qa")) {
            return Key.QA;
        } else {
            return Key.TOPIC;
        }
    }

    public static String getID(String url) {
        if (!url.startsWith("http://psnine.com")) {
            return url;
        }
        String pattern = "http://psnine.com/.*/(\\d+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        if (m.find()) {
            return m.group(1);
        } else {
            throw new IllegalArgumentException("unable to find topic id");
        }
    }
}
