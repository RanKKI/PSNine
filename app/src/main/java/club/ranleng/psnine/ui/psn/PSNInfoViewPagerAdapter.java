package club.ranleng.psnine.ui.psn;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.ui.topics.base.TopicsFragment;

public class PSNInfoViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private String[] titles = new String[]{"PSN游戏", "留言板", "主题", "基因"};

    public PSNInfoViewPagerAdapter(@NonNull String psnID, FragmentManager fm) {
        super(fm);
        fragments.add(TopicsFragment.newInstance(Key.PSNGAMES, null, psnID));
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
