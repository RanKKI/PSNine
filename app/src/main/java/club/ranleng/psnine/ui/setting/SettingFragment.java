package club.ranleng.psnine.ui.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.IntentUtils;
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
        findPreference(Key.getSetting().KEY_PREF_ABOUT).setOnPreferenceClickListener(this);
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            findPreference(Key.getSetting().KEY_VERSIONS).setSummary(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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
        String key = preference.getKey();
        if (key.equals(Key.getSetting().KEY_PREF_TABS)) {
            ToastUtils.showShort(R.string.not_support);
        } else if (key.equals(Key.getSetting().KEY_PREF_ABOUT)) {
            ActivityUtils.startActivity(IntentUtils.getAppDetailsSettingsIntent(getActivity().getPackageName()));
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