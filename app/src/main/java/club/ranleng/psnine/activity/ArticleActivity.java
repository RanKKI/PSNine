package club.ranleng.psnine.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.post.ReplyActivity;
import club.ranleng.psnine.activity.post.newGeneActivity;
import club.ranleng.psnine.activity.post.newTopicActivity;
import club.ranleng.psnine.adapter.Binder.Article.ArticleGameListBinder;
import club.ranleng.psnine.adapter.Binder.Article.ArticleHeaderBinder;
import club.ranleng.psnine.adapter.Binder.Article.ArticleReplyBinder;
import club.ranleng.psnine.adapter.Binder.ImageBinder;
import club.ranleng.psnine.adapter.Binder.MutilPagesBinder;
import club.ranleng.psnine.adapter.Binder.TextEditableItemBinder;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.model.Article.ArticleGameList;
import club.ranleng.psnine.model.Article.ArticleHeader;
import club.ranleng.psnine.model.Article.ArticleReply;
import club.ranleng.psnine.model.Article.MutilPages;
import club.ranleng.psnine.model.Image_Gene;
import club.ranleng.psnine.model.TextSpannedItem;
import club.ranleng.psnine.utils.MakeToast;
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
import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ArticleActivity extends BaseActivity {

    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    private int type;
    private int article_id;
    private Boolean f_game = true;
    private Boolean f_reply = true;
    private Article article;
    private Items items;
    private MultiTypeAdapter adapter;
    private int max_pages = 1;
    private Boolean editable = false;
    private String original = null;
    private int current_page = 1;
    private Context context;

    @Override
    public void setContentView() {
        setContentView(R.layout.view_toolbar_recycler);
    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        adapter = new MultiTypeAdapter();
        items = new Items();

        adapter.register(Line.class, new LineViewBinder());
        adapter.register(Image_Gene.class, new ImageBinder());
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(ArticleReply.class, new ArticleReplyBinder());
        adapter.register(ArticleHeader.class, new ArticleHeaderBinder());
        adapter.register(TextSpannedItem.class, new TextEditableItemBinder());
        adapter.register(ArticleGameList.class, new ArticleGameListBinder());
        adapter.register(MutilPages.class, new MutilPagesBinder(new MutilPagesBinder.OnPageChange() {
            @Override
            public void onpagechage(int page) {
                current_page = page;
                getData();
            }
        }));
        adapter.setItems(items);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        this.type = intent.getIntExtra("type", -1);
        this.article_id = intent.getIntExtra("id", -1);
        article = Internet.retrofit.create(Article.class);
        context = this;
        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_article_edit) {

            Intent intent;
            if (type == KEY.TYPE_GENE) {
                intent = new Intent(this, newGeneActivity.class);
            } else {
                intent = new Intent(this, newTopicActivity.class);
            }
            intent.putExtra("edit", true);
            intent.putExtra("id", article_id);
            startActivity(intent);

        } else if (id == R.id.action_article_reply) {
            Replies();
        } else if (id == R.id.action_artivle_fav) {
            FormBody.Builder body = new FormBody.Builder();
            body.add("type", KEY.INT_TYPE(type))
                    .add("param", String.valueOf(article_id))
                    .add("updown", "up");
            article.Fav(body.build()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    MakeToast.str("成功");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else if (id == R.id.action_article_original) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(original);
            intent.setData(content_url);
            startActivity(intent);
        } else if (id == R.id.action_artivle_up) {
            up(KEY.INT_TYPE(type), String.valueOf(article_id));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final ArticleReply articleReply = (ArticleReply) items.get(item.getGroupId());
        switch (item.getItemId()) {
            case R.id.adapter_article_menu_edit:
                Replies(null, articleReply.title, articleReply.comment_id);
                return true;
            case R.id.adapter_article_menu_reply:
                Replies(articleReply.username);
                return true;
            case R.id.adapter_article_menu_up:
                up("comment", articleReply.comment_id);
                return true;
            case R.id.adapter_article_menu_user:
                Intent intent = new Intent(context, PSNActivity.class);
                intent.putExtra("psnid", articleReply.username);
                context.startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.activity_article, menu);
        if(UserStatus.isLogin()){
            menu.findItem(R.id.action_article_reply).setVisible(true);
            menu.findItem(R.id.action_artivle_fav).setVisible(true);
            menu.findItem(R.id.action_artivle_up).setVisible(type != KEY.TYPE_GENE);
            menu.findItem(R.id.action_article_edit).setVisible(editable);
            menu.findItem(R.id.action_article_original).setVisible(original != null);

        }
        return true;
    }

    private void initData() {
        items = new Items();
        swipeRefreshLayout.setRefreshing(true);
        Observable<ResponseBody> header;
        if (type == KEY.TYPE_GENE) {
            header = article.getGene(article_id);
        } else {
            header = article.getArticle(article_id, current_page);
        }

        header.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ArrayList<Map<String, Object>>>() {
                    @Override
                    public ArrayList<Map<String, Object>> apply(@NonNull ResponseBody responseBody) throws Exception {
                        return ParseWeb.parseArticle(responseBody.string(), type);
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
                        Object map_type = map.get("type");
                        if (map_type.equals("header")) {

                            max_pages = (int) map.get("page_size");

                            if (max_pages != 1) {
                                ArrayList<String> l = new ArrayList<>();
                                for (int i = 1; i < max_pages + 1; i++) {
                                    l.add((String) map.get("page_" + String.valueOf(i)));
                                }
                                items.add(new MutilPages(l));
                                items.add(new Line());
                            }

                            editable = (Boolean) map.get("editable");
                            items.add(new ArticleHeader(map));

                            ArrayList<String> imgs = new ArrayList<String>();
                            for (int i = 0; i < (int) map.get("img_size"); i++) {
                                String url = (String) map.get("img_" + String.valueOf(i));
                                imgs.add(url);
                            }

                            items.add(new Image_Gene(imgs));

                            if (map.get("video") != null) {
                                items.add(new TextSpannedItem(String.format("<a href=\"%s\">打开视频链接</a>", map.get("video"))));
                                items.add(new Line());
                            }

                            if (map.get("original") != null)
                                original = (String) map.get("original");

                        } else if (map_type.equals("game_list")) {

                            if (f_game) {
                                f_game = false;
                                items.add(new Category("游戏列表"));
                            }
                            items.add(new ArticleGameList(map));
                        } else if (map_type.equals("reply")) {
                            if (f_reply) {
                                f_reply = false;
                                items.add(new Category("回复"));
                                items.add(new Line());
                            }
                            items.add(new ArticleReply(map));
                        }
                        items.add(new Line());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        invalidateOptionsMenu();
                        adapter.setItems(items);
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void Replies() {
        Replies(null);
    }

    private void Replies(String username) {
        Replies(username, null, null);
    }

    private void Replies(String username, String content, String comment_id) {
        Intent intent = new Intent(context, ReplyActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", article_id);

        if (username != null) {
            intent.putExtra("username", username);
        }

        if (content != null) {
            intent.putExtra("edit", true);
            intent.putExtra("content", content);
            intent.putExtra("comment_id", comment_id);
        }

        startActivity(intent);
    }


    private void up(final String type, final String param) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage("要付出4铜币来顶一下吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FormBody.Builder body = new FormBody.Builder();
                        body.add("type", type)
                                .add("param", param)
                                .add("updown", "up");
                        article.Up(body.build()).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                MakeToast.str("成功");
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                }).create();
        alertDialog.show();
    }

    interface Article {

        @GET("topic/{id}")
        Observable<ResponseBody> getArticle(@Path("id") int id, @Query("page") int page);

        @GET("gene/{id}")
        Observable<ResponseBody> getGene(@Path("id") int id);


        @POST("set/fav/ajax")
        Call<ResponseBody> Fav(@Body FormBody body);

        @POST("set/updown/ajax")
        Call<ResponseBody> Up(@Body FormBody body);

        //TODO
        //多页评论
        @GET("topic/{id}/comment")
        Observable<ResponseBody> getArticleComment(@Path("id") int id);

        @GET("gene/{id}/comment")
        Observable<ResponseBody> getGeneComment(@Path("id") int id);
    }
}
