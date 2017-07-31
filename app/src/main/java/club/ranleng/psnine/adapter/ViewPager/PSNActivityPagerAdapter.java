package club.ranleng.psnine.adapter.ViewPager;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.fragment.PSNFragment;
import club.ranleng.psnine.widget.KEY;

public class PSNActivityPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>(); //切換頁面的Fragments
    private String[] titles = {
            "PSN游戏", "留言板", "主题", "基因"
    };
    private String name;

    public PSNActivityPagerAdapter(FragmentManager fm, String name) {
        super(fm);
        this.name = name;
        fragments.add(PSNFragment.newInstance(KEY.PSN_GAME,name));
        fragments.add(PSNFragment.newInstance(KEY.PSN_MSG,name));
        fragments.add(PSNFragment.newInstance(KEY.TYPE_TOPIC,name));
        fragments.add(PSNFragment.newInstance(KEY.TYPE_GENE,name));
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

    @Override
    public void notifyDataSetChanged(){
        fragments.clear();
        fragments.add(PSNFragment.newInstance(KEY.PSN_GAME,name));
        fragments.add(PSNFragment.newInstance(KEY.PSN_MSG,name));
        fragments.add(PSNFragment.newInstance(KEY.TYPE_TOPIC,name));
        fragments.add(PSNFragment.newInstance(KEY.TYPE_GENE,name));
    }
}
