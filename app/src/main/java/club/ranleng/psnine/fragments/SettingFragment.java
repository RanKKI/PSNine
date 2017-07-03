package club.ranleng.psnine.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.SettingActivity;
import club.ranleng.psnine.util.LogUtil;

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private Map<String, String> ob = new HashMap<String, String>(){{
        put("date","最新");
        put("obdate","综合排序");
    }};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings_general);
        findPreference(SettingActivity.KEY_PREF_OB).setSummary(ob.get(SettingActivity.PREF_OB));
        findPreference(SettingActivity.KEY_PREF_OB).setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference.getKey().equals(SettingActivity.KEY_PREF_OB)){
            preference.setSummary(ob.get(newValue));
            return true;
        }
        return false;
    }


}

