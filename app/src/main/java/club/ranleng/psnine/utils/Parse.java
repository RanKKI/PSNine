package club.ranleng.psnine.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import club.ranleng.psnine.common.Key;

public class Parse {

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

    /**
     * Parse html with setting, like the size of image.
     *
     * @param text original html
     * @return parsed html
     */
    public static String parseHtml(String text) {
        return text;
    }

    /**
     * Parse image form sinaimg
     *
     * @param url original image url
     * @param quality 0:原图,1:中等尺寸,2:缩略图
     * @return parsed url
     */
    public static String parseImageUrl(String url, int quality) {
        String[] sizes = new String[]{"large", "mw690", "thumbnail"};
        String pattern = "http://.+\\.sinaimg\\.cn/(.+)/.+\\.jpg";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        if (m.find()) {
            url = url.replace(m.group(1), sizes[quality]);
        }
        return url;
    }
}
