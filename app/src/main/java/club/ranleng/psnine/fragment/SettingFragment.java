package club.ranleng.psnine.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.util.HashMap;
import java.util.Map;

import club.ranleng.psnine.R;
import club.ranleng.psnine.fragment.widget.EditTabsDialogFragment;
import club.ranleng.psnine.widget.KEY;

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private Map<String, String> ob = new HashMap<String, String>() {{
        put("date", "最新");
        put("obdate", "综合排序");
    }};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings_general);
        findPreference(KEY.KEY_PREF_OB).setSummary(ob.get(KEY.PREF_OB));
        findPreference(KEY.KEY_PREF_TABS).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                EditTabsDialogFragment dialogFragment = new EditTabsDialogFragment();
                dialogFragment.show(getFragmentManager(),"edit tabs");
                return true;
            }
        });
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


}

