package club.ranleng.psnine.module.topics;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseTabsFragment;
import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.common.UserState;

public class TopicsTabsFragment extends BaseTabsFragment implements SearchView.OnQueryTextListener {

    private List<Integer> list = KEY.getTabs();
    private int type = list.get(0);

    public static TopicsTabsFragment newInstance(){
        return new TopicsTabsFragment();
    }

    @Override
    protected FragmentPagerAdapter getAdapter(FragmentManager manager) {
        return new PagerAdapter(manager);
    }

    @Override
    protected void onItemClick(int id) {

    }

    @Override
    protected void CreateMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_topics_tabs, menu);
        menu.findItem(R.id.action_new_topic).setVisible(UserState.isLogin());
        menu.findItem(R.id.action_new_gene).setVisible(UserState.isLogin());

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    protected void ViewPagerSelected(int position) {
        type = list.get(position);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent(getActivity(), TopicsActivity.class);
        intent.putExtra("query", query);
        intent.putExtra("type", type);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}

class PagerAdapter extends FragmentPagerAdapter{

    private List<Fragment> fragments = new ArrayList<>(); //切換頁面的Fragments

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        for (int type: KEY.getTabs()) {
            fragments.add(TopicsFragment.newInstance(type,null,null));
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
        return KEY.getTypeNameCN(fragments.get(position).getArguments().getInt("type"));
    }

}