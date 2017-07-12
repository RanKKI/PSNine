package club.ranleng.psnine.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ran on 12/07/2017.
 */

public class KEY {

    public static final int SEARCH = 7557405;
    public static final int PICKIMG = 6045224;
    public static final int SETTING = 6262658;
    public static final int IMAGE = 8390671;

    //PREF KEY
    public static final String KEY_PREF_PRELOAD = "settings_preload";

    public static final String KEY_PREF_OB = "settings_ob";

    public static final String KEY_PREF_EMOJI = "settings_emoji_dialog";

    public static final String KEY_PREF_SINGLELINE = "settings_single_line";

    public static Boolean PREF_PRELOAD = false;
    public static String PREF_OB = "obdate";
    public static Boolean PREF_EMOJI = true;
    public static Boolean PREF_SINGLELINE = true;

    public static void initSetting(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        PREF_PRELOAD = sharedPref.getBoolean(KEY.KEY_PREF_PRELOAD, false);
        PREF_OB = sharedPref.getString(KEY.KEY_PREF_OB, "obdate");
        PREF_EMOJI = sharedPref.getBoolean(KEY.KEY_PREF_EMOJI, true);
        PREF_SINGLELINE = sharedPref.getBoolean(KEY.KEY_PREF_SINGLELINE, true);
    }

}
