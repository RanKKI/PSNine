package club.ranleng.psnine.utils;

import android.net.Uri;

import com.blankj.utilcode.util.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import club.ranleng.psnine.R;
import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.common.KeyGetter;

public class Parse {

    public static final String sinaimg_pattern = "http(|s)://.+\\.sinaimg\\.cn/(.+)/(.+)\\.(jpg|gif)";

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
        int q = Key.getSetting().PREF_IMAGES_QUALITY ? 0 : 2;
        text = parseImageUrl(text, q);
        return text;
    }

    public static String parseNoticeHtml(String html) {
        html = html.replace("<br>", "").replace("\\n", "");
        html = parseImageUrl(html, -1);
        return html;
    }

    /**
     * Parse image form sinaimg_pattern
     *
     * @param url     original image url
     * @param quality 0:原图,1:中等尺寸,2:缩略图,-1:缩写,-2:保留key
     * @return parsed url
     */
    public static String parseImageUrl(String url, int quality) {
        String[] sizes = new String[]{"large", "mw690", "thumbnail"};
        Pattern r = Pattern.compile(sinaimg_pattern);
        Matcher m = r.matcher(url);
        if (m.find()) {
            if (quality == -1) {
                url = url.replace(m.group(0), "[图片]");
                return url;
            } else if (quality == -2) {
                return m.group(3);
            }
            url = url.replace(m.group(2), sizes[quality]);
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

    public static String parseNodeForNewTopic(int type) {
        if (type == Key.TOPIC) {
            return "talk";
        } else {
            return KeyGetter.getKEY(type);
        }
    }

    public static Uri parsePSNStoreUrl(String psnineStoreUrl, String region) {
        String baseStoreUrl = "https://store.playstation.com/%s/product/%s";
        String key = psnineStoreUrl.split("/dd/")[1];
        String regionKey = getRegionKeyByName(region);
        return Uri.parse(String.format(baseStoreUrl, regionKey, key));
    }

    public static String getRegionKeyByName(String name) {
        switch (name) {
            case "英服":
                return "eb-gb";
            case "国服":
                return "zh-hans-cn";
            case "日服":
                return "ja-jp";
            case "美服":
                return "en-us";
            case "港服":
                return "zh-hans-hk";
            default:
                return "en-us";
        }
    }
}
