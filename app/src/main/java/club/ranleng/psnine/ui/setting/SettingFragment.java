package club.ranleng.psnine.ui.setting;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.blankj.utilcode.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import club.ranleng.psnine.R;
import club.ranleng.psnine.common.Key;

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
        addPreferencesFromResource(R.xml.settings);
        findPreference(Key.getSetting().KEY_PREF_OB).setSummary(ob.get(Key.getSetting().PREF_OB));
        findPreference(Key.getSetting().KEY_PREF_TABS).setOnPreferenceClickListener(this);
        findPreference(Key.getSetting().KEY_PREF_OB).setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(Key.getSetting().KEY_PREF_OB)) {
            String value = (String) newValue;
            preference.setSummary(ob.get(value));
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals(Key.getSetting().KEY_PREF_TABS)){
            ToastUtils.showShort(R.string.not_support);
        }
//        TabsPreference.newInstance().show(getFragmentManager(),"edit tabs");
        return false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Key.getSetting().load();
    }
}