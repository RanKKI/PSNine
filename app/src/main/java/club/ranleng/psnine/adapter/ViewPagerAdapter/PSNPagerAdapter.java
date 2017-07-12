package club.ranleng.psnine.adapter.ViewPagerAdapter;



import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.List;

public class PSNPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments; //切換頁面的Fragments
    private String[] titles = {
            "PSN游戏", "留言板", "主题", "基因"
    };

    public PSNPagerAdapter(FragmentManager fm, List<Fragment> f) {
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
