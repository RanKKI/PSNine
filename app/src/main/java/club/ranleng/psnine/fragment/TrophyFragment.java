package club.ranleng.psnine.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.Binder.PSNGameHeaderBinder;
import club.ranleng.psnine.adapter.Binder.PSNGameTrophyBinder;
import club.ranleng.psnine.adapter.Binder.PSNGameUserBinder;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.model.PSNGameHeader;
import club.ranleng.psnine.model.PSNGameTrophy;
import club.ranleng.psnine.model.PSNGameUser;
import club.ranleng.psnine.widget.Internet;
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
import retrofit2.http.Path;
import retrofit2.http.Query;


public class TrophyFragment extends BaseFragment {

    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    private Context context;
    private MultiTypeAdapter adapter;
    private Items items;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.view_recycler, container, false);
        ButterKnife.bind(this, view);
        context = inflater.getContext();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new MultiTypeAdapter();
        adapter.register(PSNGameTrophy.class, new PSNGameTrophyBinder());
        adapter.register(PSNGameHeader.class, new PSNGameHeaderBinder());
        adapter.register(PSNGameUser.class, new PSNGameUserBinder());
        adapter.register(Line.class, new LineViewBinder());
        adapter.register(Category.class, new CategoryViewBinder());
        return view;
    }

    @Override
    public void initData() {
        swipeRefreshLayout.setRefreshing(true);
        items = new Items();
        String game_id = getArguments().getString("game_id");
        String username = getArguments().getString("username");

        Trophy trophy = Internet.retrofit.create(Trophy.class);
        trophy.GetTrophy(game_id, username)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ArrayList<Map<String, Object>>>() {
                    @Override
                    public ArrayList<Map<String, Object>> apply(@NonNull ResponseBody responseBody) throws Exception {
                        return ParseWeb.parsePSNGameTrophy(responseBody.string());
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
                        Object type = map.get("type");

                        if (type.equals("header")) {
                            items.add(new PSNGameHeader(map));
                        } else if (type.equals("user")) {
                            items.add(new PSNGameUser(map));
                        } else {
                            items.add(new PSNGameTrophy(map));
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
        @GET("psngame/{id}")
        Observable<ResponseBody> GetTrophy(@Path("id") String id, @Query("psnid") String psnid);
    }
}
