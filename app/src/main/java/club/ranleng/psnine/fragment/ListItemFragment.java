package club.ranleng.psnine.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.Binder.ArticleListBinder;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.event.LoadEvent;
import club.ranleng.psnine.model.ArticleListModel;
import club.ranleng.psnine.utils.MakeToast;
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
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public class ListItemFragment extends BaseFragment {

    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    private MultiTypeAdapter adapter;
    private Items items;
    private String search_word;
    private int type;
    private int current_page = 1;
    private int max_page = 1;
    private int itemCount;
    private int lastPosition;
    private int lastItemCount;

    public static ListItemFragment newInstance(int type, String query) {
        ListItemFragment listItemFragment = new ListItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("search_word", query);
        listItemFragment.setArguments(bundle);
        return listItemFragment;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.view_recycler, container,false);
        ButterKnife.bind(this, view);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(inflater.getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    itemCount = mLayoutManager.getItemCount();
                    lastPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                } else {
                    return;
                }
                if (lastItemCount != itemCount && lastPosition == itemCount - 1 && current_page != max_page) {
                    current_page++;
                    initData();
                    lastItemCount = itemCount;
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items = new Items();
                initData();
            }
        });
        swipeRefreshLayout.setDistanceToTriggerSync(500);
        this.type = getArguments().getInt("type");
        this.search_word = getArguments().getString("search_word");

        if (search_word == null) search_word = "";

        adapter = new MultiTypeAdapter();
        adapter.register(ArticleListModel.class, new ArticleListBinder());
        adapter.register(Line.class, new LineViewBinder());
        items = new Items();
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void initData() {
        swipeRefreshLayout.setRefreshing(true);
        ArticleList articleList = Internet.retrofit.create(ArticleList.class);
        Observable<ResponseBody> observable = null;
        if (type == KEY.TYPE_TOPIC || type == KEY.TYPE_GENE || type == KEY.TYPE_QA) {
            observable = articleList.getTopic(KEY.TYPE_NAME.get(type), KEY.PREF_OB, search_word, current_page);
        } else if (type == KEY.TYPE_GUIDE || type == KEY.TYPE_PLUS || type == KEY.TYPE_OPENBOX) {
            observable = articleList.getNode(KEY.TYPE_NAME.get(type), KEY.PREF_OB, search_word, current_page);
        } else if (type == KEY.TYPE_NOTICE) {
            observable = articleList.getNotice();
        }

        assert observable != null;
        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ArrayList<Map<String, Object>>>() {
                    @Override
                    public ArrayList<Map<String, Object>> apply(@NonNull ResponseBody responseBody) throws Exception {
                        return ParseWeb.parseArticleList(responseBody.string(), type);
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

                    }

                    @Override
                    public void onNext(@NonNull Map<String, Object> map) {
                        if (map.containsKey("max_page")) {
                            max_page = (int) map.get("max_page");
                            return;
                        }
                        items.add(new ArticleListModel(map));
                        items.add(new Line());
                        adapter.notifyItemInserted(items.size());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        MakeToast.notfound();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        EventBus.getDefault().post(new LoadEvent());
//                        adapter.notifyDataSetChanged();

                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    interface ArticleList {
        @GET("{type}")
        Observable<ResponseBody> getTopic(@Path("type") String type, @Query("ob") String ob, @Query("title") String search_word, @Query("page") int page);


        @GET("node/{node}")
        Observable<ResponseBody> getNode(@Path("node") String type, @Query("ob") String ob, @Query("title") String search_word, @Query("page") int page);

        @GET("my/notice")
        Observable<ResponseBody> getNotice();
    }
}
