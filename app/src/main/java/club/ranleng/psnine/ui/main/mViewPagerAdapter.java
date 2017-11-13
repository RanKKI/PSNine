package club.ranleng.psnine.ui.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.common.KeyGetter;
import club.ranleng.psnine.ui.topics.TopicsFragment;

public class mViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>(); //切換頁面的Fragments
    private List<Integer> fragments_types = new ArrayList<Integer>() {{
        add(Key.GENE);
        add(Key.TOPIC);
        add(Key.OPENBOX);
        add(Key.QA);
        add(Key.GUIDE);
        add(Key.PLUS);
    }};

    mViewPagerAdapter(FragmentManager fm) {
        super(fm);
        for (Integer type : fragments_types) {
            fragments.add((TopicsFragment.newInstance(type)));
        }
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
        return KeyGetter.getKEYName(fragments_types.get(position));
    }

    public int getKeyByPosition(int position) {
        return fragments_types.get(position);
    }

}
