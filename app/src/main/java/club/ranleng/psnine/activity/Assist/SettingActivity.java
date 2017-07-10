package club.ranleng.psnine.activity.Assist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import club.ranleng.psnine.fragments.SettingFragment;

public class SettingActivity extends AppCompatActivity{


    public static final String KEY_PREF_PRELOAD = "settings_preload";
    public static final String KEY_PREF_OB = "settings_ob";
    public static final String KEY_PREF_EMOJI = "settings_emoji_dialog";
    public static final String KEY_PREF_SINGLELINE = "settings_single_line";
    public static final String KEY_PREF_SAVE_TEMP_REPLY = "settings_save_temp_reply";

    public static Boolean PREF_PRELOAD = false;
    public static String PREF_OB = "obdate";
    public static Boolean PREF_EMOJI = true;
    public static Boolean PREF_SINGLELINE = true;
    public static Boolean PREF_SAVE_TEMP_REPLY = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设置");
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
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
        PREF_EMOJI = sharedPref.getBoolean(KEY_PREF_EMOJI,true);
        PREF_SINGLELINE = sharedPref.getBoolean(KEY_PREF_SINGLELINE,true);
        PREF_SAVE_TEMP_REPLY = sharedPref.getBoolean(KEY_PREF_SAVE_TEMP_REPLY,true);
    }


}
