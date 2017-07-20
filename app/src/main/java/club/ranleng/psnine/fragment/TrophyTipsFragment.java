package club.ranleng.psnine.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.Binder.Article.ArticleReplyBinder;
import club.ranleng.psnine.adapter.Binder.PSNGameTrophyBinder;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.model.Article.ArticleReply;
import club.ranleng.psnine.model.PSNGameTrophy;
import club.ranleng.psnine.widget.ParseWeb;
import club.ranleng.psnine.widget.Internet;
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
import retrofit2.http.Path;

public class TrophyTipsFragment extends BaseFragment {

    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    private Context context;
    private MultiTypeAdapter adapter;
    private Items items;

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.view_recycler, null);
        context = inflater.getContext();
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new MultiTypeAdapter();
        adapter.register(PSNGameTrophy.class, new PSNGameTrophyBinder());
        adapter.register(Line.class, new LineViewBinder());
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(ArticleReply.class, new ArticleReplyBinder());
        return view;
    }

    @Override
    public void initData() {
        swipeRefreshLayout.setRefreshing(true);
        items = new Items();
        String trophy_id = getArguments().getString("trophy_id");
        Trophy trophy = Internet.retrofit.create(Trophy.class);
        trophy.GetTrophyTips(trophy_id).subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ArrayList<Map<String, Object>>>() {
                    @Override
                    public ArrayList<Map<String, Object>> apply(@NonNull ResponseBody responseBody) throws Exception {
                        return ParseWeb.parsePSNGameTrophyTips(responseBody.string());
                    }
                })
                .flatMapIterable(new Function<ArrayList<Map<String, Object>>, Iterable<Map<String, Object>>>() {
                    @Override
                    public Iterable<Map<String, Object>> apply(@NonNull ArrayList<Map<String, Object>> maps) throws Exception {
                        return maps;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<String, Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        adapter.setItems(items);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onNext(@NonNull Map<String, Object> map) {
                        if (map.containsKey("trophy_icon")) {
                            items.add(new PSNGameTrophy(map));
                        } else {
                            items.add(new ArticleReply(map));
                        }

                        items.add(new Line());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    interface Trophy {
        @GET("trophy/{id}")
        Observable<ResponseBody> GetTrophyTips(@Path("id") String id);
    }
}
