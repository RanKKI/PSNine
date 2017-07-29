package club.ranleng.psnine.fragment.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.FragActivity;
import club.ranleng.psnine.activity.post.newGeneActivity;
import club.ranleng.psnine.activity.post.newTopicActivity;
import club.ranleng.psnine.adapter.MainActivityPagerAdapter;
import club.ranleng.psnine.widget.KEY;
import club.ranleng.psnine.widget.UserStatus;

public class MainTabsFragment extends Fragment {

    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;

    private int[] tabs_keys = {KEY.TYPE_GENE, KEY.TYPE_TOPIC, KEY.TYPE_OPENBOX, KEY.TYPE_GUIDE, KEY.TYPE_PLUS};
    private Context context;
    private View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.view_pager_tabs, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        ButterKnife.bind(this, view);
        context = inflater.getContext();
        viewPager.setAdapter(new MainActivityPagerAdapter(getFragmentManager()));  //設定Adapter給viewPager
        tabLayout.setupWithViewPager(viewPager); //绑定viewPager

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_new_topic).setVisible(UserStatus.isLogin());
        menu.findItem(R.id.action_new_gene).setVisible(UserStatus.isLogin());

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                Intent intent = new Intent(context, FragActivity.class);
                intent.putExtra("key", KEY.SEARCH);
                intent.putExtra("query", query);
                intent.putExtra("type", tabs_keys[tabLayout.getSelectedTabPosition()]);
                startActivity(intent);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_new_gene) {
            startActivity(new Intent(context, newGeneActivity.class));
        } else if (id == R.id.action_new_topic) {
            startActivity(new Intent(context, newTopicActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
