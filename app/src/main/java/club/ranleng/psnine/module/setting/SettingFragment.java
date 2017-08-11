package club.ranleng.psnine.module.setting;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.util.HashMap;
import java.util.Map;

import club.ranleng.psnine.R;
import club.ranleng.psnine.common.KEY;

public class SettingFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private Map<String, String> ob = new HashMap<String, String>() {{
        put("date", "最新");
        put("obdate", "综合排序");
    }};

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_general);
        findPreference(KEY.KEY_PREF_OB).setSummary(ob.get(KEY.PREF_OB));
        findPreference(KEY.KEY_PREF_TABS).setOnPreferenceClickListener(this);
        findPreference(KEY.KEY_PREF_OB).setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(KEY.KEY_PREF_OB)) {
            preference.setSummary(ob.get(newValue));
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        TabsPreference.newInstance().show(getFragmentManager(),"edit tabs");
        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            KEY.initSetting();
        }
    }

}
