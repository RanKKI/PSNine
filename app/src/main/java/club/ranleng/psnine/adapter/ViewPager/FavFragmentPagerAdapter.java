package club.ranleng.psnine.adapter.ViewPager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.fragment.ListItemFragment;
import club.ranleng.psnine.widget.KEY;

public class FavFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>(); //切換頁面的Fragments
    private String[] titles = {
            "主题", "基因", "用户", "问答"
    };

    public FavFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(ListItemFragment.newInstance(KEY.TYPE_FAV_TOPIC, null, null));
        fragments.add(ListItemFragment.newInstance(KEY.TYPE_FAV_GENE, null, null));
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
