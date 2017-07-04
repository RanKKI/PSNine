package club.ranleng.psnine.activity.Assist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import club.ranleng.psnine.fragments.SettingFragment;

public class SettingActivity extends AppCompatActivity{


    public static final String KEY_PREF_PRELOAD = "settings_preload";
    public static final String KEY_PREF_OB = "settings_ob";
    public static Boolean PREF_PRELOAD = false;
    public static String PREF_OB = "obdate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设置");
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();
    }

    @Override
    protected void onPause(){
        super.onPause();
        initSetting(this);
    }

    public static void initSetting(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        PREF_PRELOAD = sharedPref.getBoolean(KEY_PREF_PRELOAD,false);
        PREF_OB = sharedPref.getString(KEY_PREF_OB,"obdate");
    }


}
