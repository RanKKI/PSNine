package club.ranleng.psnine.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.database.DataSetObserver;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.fragment.ListItemFragment;
import club.ranleng.psnine.widget.KEY;

public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments; //切換頁面的Fragments
    private List<Integer> list;

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
        list = KEY.getTabs();
        fragments = new ArrayList<>();
        for(Integer i : list){
            fragments.add(newListItem(i));
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
        return KEY.TYPE_NAME_CN.get(list.get(position));
    }

    @Override
    public void notifyDataSetChanged(){
        fragments.clear();
        list = KEY.getTabs();
        for(Integer i : list){
            fragments.add(newListItem(i));
        }
    }

    private Fragment newListItem(int type) {
        return ListItemFragment.newInstance(type, null);
    }
}
