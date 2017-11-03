package club.ranleng.psnine.utils;

import com.blankj.utilcode.util.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import club.ranleng.psnine.R;
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
     * Parse html with settings, like the size of image.
     *
     * @param text original html
     * @return parsed html
     */
    public static String parseHtml(String text) {
        int q = Key.getSetting().PREF_IMAGESQUALITY ? 0 : 2;
        text = parseImageUrl(text, q);
        return text;
    }

    /**
     * Parse image form sinaimg
     *
     * @param url     original image url
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

    public static String parseHTTPErrorCode(int code) {
        String error_message;
        switch (code) {
            case 400:
                error_message = Utils.getApp().getString(R.string.http_code_bad_request);
                break;
            case 401:
                error_message = Utils.getApp().getString(R.string.http_code_unauthorized);
                break;
            case 402:
                error_message = Utils.getApp().getString(R.string.http_code_payment_required);
                break;
            case 403:
                error_message = Utils.getApp().getString(R.string.http_code_forbidden);
                break;
            case 404:
                error_message = Utils.getApp().getString(R.string.http_code_not_found);
                break;
            case 405:
                error_message = Utils.getApp().getString(R.string.http_code_method_not_allowed);
                break;
            case 406:
                error_message = Utils.getApp().getString(R.string.http_code_not_acceptable);
                break;
            case 500:
                error_message = Utils.getApp().getString(R.string.http_code_internal_server_error);
                break;
            case 501:
                error_message = Utils.getApp().getString(R.string.http_code_not_implemented);
                break;
            case 502:
                error_message = Utils.getApp().getString(R.string.http_code_bad_gateway);
                break;
            case 503:
                error_message = Utils.getApp().getString(R.string.http_code_service_unavailable);
                break;
            case 504:
                error_message = Utils.getApp().getString(R.string.http_code_gateway_timeout);
                break;
            default:
                error_message = Utils.getApp().getString(R.string.http_code_unknown_error);
                break;
        }
        return error_message;
    }
}