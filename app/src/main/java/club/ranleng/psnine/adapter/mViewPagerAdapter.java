package club.ranleng.psnine.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.topics.TopicsFragment;

public class mViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>(); //切換頁面的Fragments

    public mViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add((TopicsFragment.newInstance()));
        fragments.add((TopicsFragment.newInstance()));
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
        return "test-" + String.valueOf(position);
    }

}
