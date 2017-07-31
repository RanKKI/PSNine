package club.ranleng.psnine.fragment.main;

import android.app.Fragment;
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
import club.ranleng.psnine.adapter.Binder.Common.CardViewBinder;
import club.ranleng.psnine.model.Card;
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
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.view_recycler, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        ButterKnife.bind(this, view);

        swipeRefreshLayout.setEnabled(false);
        MultiTypeAdapter adapter = new MultiTypeAdapter();
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(Card.class, new CardViewBinder(null));
        adapter.register(Line.class, new LineViewBinder());
        adapter.register(Contributor.class, new ContributorViewBinder());
        adapter.register(License.class, new LicenseViewBinder());
        adapter.setItems(initItems());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    private Items initItems() {
        Items items = new Items();

        items.add(new Category("关于PSNine"));
        items.add(new Card(getString(R.string.about_p9_content)));

        items.add(new Category("关于本软件"));
        items.add(new Card(getString(R.string.about_content)));

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
