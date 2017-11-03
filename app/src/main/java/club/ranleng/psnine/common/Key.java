package club.ranleng.psnine.common;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.blankj.utilcode.util.Utils;

public class Key {

    //about type of topic
    public static final int TOPIC = 1448;
    public static final int GENE = 1449;
    public static final int PLUS = 1450;
    public static final int OPENBOX = 1451;
    public static final int GUIDE = 1452;
    public static final int NOTICE = 1453;
    public static final int QA = 1454;

    //about permission
    public static int REQUEST_PERMISSION = 2450;

    private static Setting setting;

    public static Setting getSetting() {
        if (setting == null) {
            setting = new Setting();
        }
        return setting;
    }

    public static class Setting {

        public final String KEY_PREF_PRELOAD = "settings_preload";
        public final String KEY_PREF_OB = "settings_ob";
        public final String KEY_PREF_EMOJI = "settings_emoji_dialog";
        public final String KEY_PREF_SINGLELINE = "settings_single_line";
        public final String KEY_PREF_IMAGES_QUALITY = "settings_images_quality";
        public final String KEY_PREF_TABS = "settings_tabs";
        public Boolean PREF_PRELOAD = true;
        public String PREF_OB = "obdate";
        public Boolean PREF_EMOJI = true;
        public Boolean PREF_SINGLELINE = true;
        public Boolean PREF_IMAGESQUALITY = true;

        Setting() {
            load();
        }

        public void load() {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Utils.getApp());
            PREF_PRELOAD = sharedPref.getBoolean(KEY_PREF_PRELOAD, false);
            PREF_OB = sharedPref.getString(KEY_PREF_OB, "obdate");
            PREF_EMOJI = sharedPref.getBoolean(KEY_PREF_EMOJI, true);
            PREF_SINGLELINE = sharedPref.getBoolean(KEY_PREF_SINGLELINE, true);
            PREF_IMAGESQUALITY = sharedPref.getBoolean(KEY_PREF_IMAGES_QUALITY, true);
        }
    }
}
