package club.ranleng.psnine.module.about;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.common.multitype.binder.CardViewBinder;
import club.ranleng.psnine.common.multitype.model.CardBean;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.CategoryViewBinder;
import me.drakeet.support.about.Contributor;
import me.drakeet.support.about.ContributorViewBinder;
import me.drakeet.support.about.License;
import me.drakeet.support.about.LicenseViewBinder;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;

public class AboutFragment extends Fragment {


    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;

    private Context context;

    public AboutFragment() {

    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        MultiTypeAdapter adapter = new MultiTypeAdapter();
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(CardBean.class, new CardViewBinder(null));
        adapter.register(Line.class, new LineViewBinder());
        adapter.register(Contributor.class, new ContributorViewBinder());
        adapter.register(License.class, new LicenseViewBinder());
        adapter.setItems(initItems());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    private Items initItems() {
        Items items = new Items();

        items.add(new Category("关于PSNine"));
        items.add(new CardBean(getString(R.string.about_p9_content)));

        items.add(new Category("关于本软件"));
        items.add(new CardBean(getString(R.string.about_content)));

        items.add(new Line());

        items.add(new Category("Developers"));
        items.add(new Contributor(R.drawable.icon, "RanKKI", "Developer & designer", "https://github.com/RanKKI"));
        items.add(new Line());

        items.add(new Category("Open Source Licenses"));
        items.add(new License("Retrofit", "Square", License.APACHE_2, "https://github.com/square/retrofit"));
        items.add(new License("Glide", "SamJudd", "BSD, part MIT and Apache 2.0", "https://github.com/bumptech/glide"));
        items.add(new License("RxJava", "ReactiveX", License.APACHE_2, "https://github.com/ReactiveX/RxJava"));
        items.add(new License("RxAndroid", "ReactiveX", License.APACHE_2, "https://github.com/ReactiveX/RxAndroid"));
        items.add(new License("PersistentCookieJar", "franmontiel", License.APACHE_2, "https://github.com/franmontiel/PersistentCookieJar"));
        items.add(new License("MultiType", "Drakeet", License.APACHE_2, "https://github.com/drakeet/MultiType"));
        items.add(new License("about-page", "Drakeet", License.APACHE_2, "https://github.com/drakeet/about-page"));
        items.add(new License("AndroidUtilCode", "Blankj", License.APACHE_2, "https://github.com/Blankj/AndroidUtilCode"));
        items.add(new License("EventBus", "greenrobot", License.APACHE_2, "https://github.com/greenrobot/EventBus"));
        return items;
    }

}
