package club.ranleng.psnine.common;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.blankj.utilcode.util.Utils;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.utils.SharedPerferenceUtils;

public class KEY {

    public static final int TOPIC = 1448;
    public static final int GENE = 6107;
    public static final int GENE_FAV = 999;
    public static final int TOPIC_FAV = 998;
    public static final int PLUS = 7890;
    public static final int OPENBOX = 5427;
    public static final int GUIDE = 9820;
    public static final int NOTICE = 7775;
    public static final int COMMENT = 1238;
    public static final int QA = 1218;

    public static final int PHOTO_LOCAL = 123;
    public static final int PHOTO_CAMERA = 124;
    public static final int REQUEST_PERMISSION_READ_EXTERNAL = 21;

    public static final String KEY_PREF_PRELOAD = "settings_preload";
    public static final String KEY_PREF_OB = "settings_ob";
    public static final String KEY_PREF_EMOJI = "settings_emoji_dialog";
    public static final String KEY_PREF_SINGLELINE = "settings_single_line";
    public static final String KEY_PREF_IMAGES_QUALITY = "settings_images_quality";
    public static final String KEY_PREF_TABS = "settings_tabs";

    public static Boolean PREF_PRELOAD = true;
    public static String PREF_OB = "obdate";
    public static Boolean PREF_EMOJI = true;
    public static Boolean PREF_SINGLELINE = true;
    public static Boolean PREF_IMAGESQUALITY = true;

    public static String getTypeName(int type) {
        if (type == GENE || type == GENE_FAV) {
            return "gene";
        } else if (type == PLUS) {
            return "plus";
        } else if (type == OPENBOX) {
            return "openbox";
        } else if (type == GUIDE) {
            return "guide";
        } else if (type == QA) {
            return "qa";
        }
        return "topic";
    }

    public static String getTypeNameCN(int type) {
        if (type == GENE || type == GENE_FAV) {
            return "基因";
        } else if (type == PLUS) {
            return "PLUS";
        } else if (type == OPENBOX) {
            return "开箱";
        } else if (type == GUIDE) {
            return "攻略";
        } else if (type == QA) {
            return "问答";
        } else if (type == TOPIC || type == TOPIC_FAV) {
            return "主页";
        }
        return "";
    }

    public static String TopicOrGene(int type) {
        if (type == GENE || type == GENE_FAV) {
            return "gene";
        }
        return "topic";
    }

    public static void initSetting() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Utils.getContext().getApplicationContext());
        PREF_PRELOAD = sharedPref.getBoolean(KEY.KEY_PREF_PRELOAD, false);
        PREF_OB = sharedPref.getString(KEY.KEY_PREF_OB, "obdate");
        PREF_EMOJI = sharedPref.getBoolean(KEY.KEY_PREF_EMOJI, true);
        PREF_SINGLELINE = sharedPref.getBoolean(KEY.KEY_PREF_SINGLELINE, true);
        PREF_IMAGESQUALITY = sharedPref.getBoolean(KEY.KEY_PREF_IMAGES_QUALITY, true);
        if (getTabs() == null) {
            List<Integer> list = new ArrayList<Integer>() {{
                add(GENE);
                add(TOPIC);
                add(OPENBOX);
                add(GUIDE);
                add(PLUS);
                add(QA);
            }};
            setTabs(list);
        }
    }

    public static List<Integer> getTabs() {
        Object object = SharedPerferenceUtils.get("tabs", "tabs");
        if (object != null) {
            return (List<Integer>) object;
        }
        return null;
    }

    public static void setTabs(List<Integer> list) {
        SharedPerferenceUtils.save("tabs", "tabs", list);
    }

}
