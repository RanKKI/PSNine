package club.ranleng.psnine.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;

public abstract class BaseTabsFragment extends Fragment {

    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.view_tabs_pager, container, false);
        ButterKnife.bind(this, view);

        FragmentPagerAdapter adapter = getAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ViewPagerSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        CreateMenu(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onItemClick(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    protected abstract FragmentPagerAdapter getAdapter(FragmentManager manager);

    protected abstract void onItemClick(int id);

    protected abstract void CreateMenu(Menu menu, MenuInflater inflater);

    protected abstract void ViewPagerSelected(int position);
}
