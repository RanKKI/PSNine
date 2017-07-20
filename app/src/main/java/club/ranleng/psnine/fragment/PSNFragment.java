package club.ranleng.psnine.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.PSNActivity;
import club.ranleng.psnine.adapter.Binder.Article.ArticleReplyBinder;
import club.ranleng.psnine.adapter.Binder.ArticleListBinder;
import club.ranleng.psnine.adapter.Binder.PSNGameListBinder;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.model.Article.ArticleReply;
import club.ranleng.psnine.model.ArticleListModel;
import club.ranleng.psnine.model.GameList;
import club.ranleng.psnine.widget.Internet;
import club.ranleng.psnine.widget.KEY;
import club.ranleng.psnine.widget.ParseWeb;
import club.ranleng.psnine.widget.UserStatus;
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

public class PSNFragment extends BaseFragment {

    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    private Context context;
    private String psnid;
    private int type;
    private Items items;
    private MultiTypeAdapter adapter;
    private int max_page = 1;

    public static PSNFragment newInstance(int type, String psnid) {
        PSNFragment psnFragment = new PSNFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("psnid", psnid);
        psnFragment.setArguments(bundle);
        return psnFragment;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.view_recycler, null, false);
        ButterKnife.bind(this, view);
        context = inflater.getContext();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items = new Items();
                initData();
            }
        });

        psnid = getArguments().getString("psnid");
        type = getArguments().getInt("type");


        adapter = new MultiTypeAdapter();
        items = new Items();
        adapter.register(Line.class,new LineViewBinder());
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(ArticleReply.class, new ArticleReplyBinder());
        adapter.register(ArticleListModel.class, new ArticleListBinder());
        adapter.register(GameList.class, new PSNGameListBinder(psnid));

        adapter.setItems(items);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void initData() {
        swipeRefreshLayout.setRefreshing(true);
        Psn psn = Internet.retrofit.create(Psn.class);

        Observable<ResponseBody> call = null;
        if (type == KEY.PSN_GAME) {
            call = psn.getGame(psnid);
        } else if (type == KEY.PSN_MSG) {
            call = psn.getMsg(psnid);
        } else if (type == KEY.TYPE_TOPIC) {
            call = psn.getTopic(psnid);
            if(UserStatus.getusername().equalsIgnoreCase(psnid)){
                call = psn.getIssueTopic();
            }

        } else if (type == KEY.TYPE_GENE) {
            call = psn.getGene(psnid);
            if(UserStatus.getusername().equalsIgnoreCase(psnid)){
                call = psn.getIssueGene();
            }
        }

        assert call != null;
        call.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ArrayList<Map<String, Object>>>() {
                    @Override
                    public ArrayList<Map<String, Object>> apply(@NonNull ResponseBody responseBody) throws Exception {
                        ArrayList<Map<String, Object>> info = ParseWeb.parsePSN(responseBody.string(), type);
                        if(info.size() == 1){
                            items.add(new Line());
                            items.add(new Category("这个人没有这页诶"));
                        }
                        return info ;
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
                        items.add(new Line());
                    }

                    @Override
                    public void onNext(@NonNull Map<String, Object> map) {
                        if (map.containsKey("max_page")) {
                            max_page = (int) map.get("max_page");
                            return;
                        }
                        if(type == KEY.TYPE_GENE || type == KEY.TYPE_TOPIC){
                            items.add(new ArticleListModel(map));
                        }else if(type == KEY.PSN_GAME){
                            items.add(new GameList(map));
                        }else if(type == KEY.PSN_MSG){
                            items.add(new ArticleReply(map));
                        }
                        items.add(new Line());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        swipeRefreshLayout.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                    }
                });


    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final ArticleReply articleReply = (ArticleReply) items.get(item.getGroupId());
        switch (item.getItemId()) {
            case R.id.adapter_article_menu_user:
                Intent intent = new Intent(context, PSNActivity.class);
                intent.putExtra("psnid", articleReply.username);
                context.startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    interface Psn {
        @GET("psnid/{id}/psngame")
        Observable<ResponseBody> getGame(@Path("id") String psnid);

        @GET("psnid/{id}/comment")
        Observable<ResponseBody> getMsg(@Path("id") String psnid);

        @GET("psnid/{id}/topic")
        Observable<ResponseBody> getTopic(@Path("id") String psnid);

        @GET("psnid/{id}/gene")
        Observable<ResponseBody> getGene(@Path("id") String psnid);

        @GET("my/issue?channel=topic")
        Observable<ResponseBody> getIssueTopic();

        @GET("my/issue?channel=gene")
        Observable<ResponseBody> getIssueGene();
    }
}
