package club.ranleng.psnine.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.FragActivity;
import club.ranleng.psnine.adapter.Binder.Common.CardViewBinder;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.model.Card;
import club.ranleng.psnine.widget.Internet;
import club.ranleng.psnine.widget.KEY;
import club.ranleng.psnine.widget.ParseWeb;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.CategoryViewBinder;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

public class ElementListFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;

    private Items items;
    private My my;
    private MultiTypeAdapter adapter;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.view_recycler, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        my = Internet.retrofit.create(My.class);

        adapter = new MultiTypeAdapter();
        adapter.register(Card.class, new CardViewBinder(this));
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(Line.class, new LineViewBinder());
        items = new Items();

        adapter.setItems(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void initData() {
        items.clear();
        my.getEle().subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, List<String>>() {
                    @Override
                    public List<String> apply(@NonNull ResponseBody responseBody) throws Exception {
                        return ParseWeb.parseElement(responseBody.string());
                    }
                })
                .flatMapIterable(new Function<List<String>, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(@NonNull List<String> strings) throws Exception {
                        return strings;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        items.add(new Category("使用过的元素"));
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        items.add(new Card(s));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), FragActivity.class);
        intent.putExtra("key", KEY.TYPE_GENE_ELE);
        intent.putExtra("ele",(String) v.getTag());
        startActivity(intent);
    }


    interface My {
        @GET("my/element")
        Observable<ResponseBody> getEle();
    }
}
