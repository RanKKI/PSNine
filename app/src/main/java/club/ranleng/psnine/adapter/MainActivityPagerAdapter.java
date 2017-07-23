package club.ranleng.psnine.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.fragment.ListItemFragment;
import club.ranleng.psnine.widget.KEY;

public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>(); //切換頁面的Fragments
    private String[] titles = {
            "基因", "首页", "开箱", "攻略", "PLUS", "PSN游戏"
    };

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(newListItem(KEY.TYPE_GENE));
        fragments.add(newListItem(KEY.TYPE_TOPIC));
        fragments.add(newListItem(KEY.TYPE_OPENBOX));
        fragments.add(newListItem(KEY.TYPE_GUIDE));
        fragments.add(newListItem(KEY.TYPE_PLUS));
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

    Fragment newListItem(int type) {
        return ListItemFragment.newInstance(type, null);
    }
}
