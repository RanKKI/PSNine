package club.ranleng.psnine.adapter.ViewPagerAdapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments; //切換頁面的Fragments
    private String[] titles = {
            "基因", "首页", "开箱", "攻略", "PLUS"
    };

    public MainPagerAdapter(FragmentManager fm, List<Fragment> f) {
        super(fm);
        fragments = f;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
