package club.ranleng.psnine.module.psn;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.base.BaseTabsFragment;
import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.module.psn.psnitem.PSNItemFragment;

public class PSNTabsFragment extends BaseTabsFragment {

    public static PSNTabsFragment newInstance(String psnid) {
        PSNTabsFragment fragment = new PSNTabsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("psnid", psnid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected FragmentPagerAdapter getAdapter(FragmentManager manager) {
        return new PagerAdapter(manager, getArguments().getString("psnid"));
    }

    @Override
    protected void CreateMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    protected void ViewPagerSelected(int position) {

    }

}

class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>(); //切換頁面的Fragments
    private String[] name = {"PSN游戏", "留言板", "主题", "基因"};

    public PagerAdapter(FragmentManager fm, String psnid) {
        super(fm);
        fragments.add(PSNItemFragment.newInstance(psnid, KEY.PSN_GAME));
        fragments.add(PSNItemFragment.newInstance(psnid, KEY.PSN_MSG));
        fragments.add(PSNItemFragment.newInstance(psnid, KEY.TOPIC));
        fragments.add(PSNItemFragment.newInstance(psnid, KEY.GENE));
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
        return name[position];
    }

}