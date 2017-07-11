package club.ranleng.psnine.activity.Main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.Post.ReplyActivity;
import club.ranleng.psnine.adapter.Article.ArticleGameListAdapter;
import club.ranleng.psnine.adapter.Article.ArticleHeaderAdapter;
import club.ranleng.psnine.adapter.Article.ArticleReplyAdapter;
import club.ranleng.psnine.adapter.Common.MutilPagesAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.model.Article.ArticleGameList;
import club.ranleng.psnine.model.Article.ArticleHeader;
import club.ranleng.psnine.model.Article.ArticleReply;
import club.ranleng.psnine.model.Article.MutilPages;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestPost;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import club.ranleng.psnine.widget.UserStatus;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.CategoryViewBinder;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;
import okhttp3.FormBody;

public class ArticleActivity extends BaseActivity
        implements RequestWebPageListener, SwipeRefreshLayout.OnRefreshListener,
        ArticleHeaderAdapter.OnItemClickListener, ArticleGameListAdapter.OnItemClickListener,
        MutilPagesAdapter.OnPageChange {

    private String type;
    private String a_id;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Boolean form_reply = false;
    private static Context context;
    private int current_page = 1;
    private int max_pages = 1;
    private Items items;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_article);
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.fragment_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
    }

    @Override
    public void setupViews() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        registerForContextMenu(recyclerView);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        a_id = intent.getStringExtra("id");
        setTitle("No. " + a_id);
        context = this;
    }


    @Override
    public void getData() {
        new RequestWebPage(this, type, a_id, true, String.valueOf(current_page));
    }

    @Override
    public void showContent() {

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
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setMessage("要付出4铜币来顶一下吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FormBody body = new FormBody.Builder()
                                        .add("type", "comment")
                                        .add("param", articleReply.comment_id)
                                        .add("updown", "up")
                                        .build();
                                new RequestPost(null, context, "updown", body);
                            }
                        }).create();
                alertDialog.show();
                return true;
            case R.id.adapter_article_menu_user:
                Intent intent = new Intent(context, PersonInfoActivity.class);
                intent.putExtra("psnid", articleReply.username);
                context.startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_article, menu);
        if (!UserStatus.isLogin()) {
            menu.findItem(R.id.action_article_reply).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_article_reply) {
            Replies();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                getData();
                form_reply = true;
            }
        }

    }

    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onPrepare() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void on404() {
        MakeToast.notfound();
        finish();
    }

    @Override
    public void onSuccess(ArrayList<Map<String, Object>> result) {
        MultiTypeAdapter adapter = new MultiTypeAdapter();
        items = new Items();

        adapter.register(Line.class, new LineViewBinder());
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(MutilPages.class, new MutilPagesAdapter(this));
        adapter.register(ArticleReply.class, new ArticleReplyAdapter());
        adapter.register(ArticleHeader.class, new ArticleHeaderAdapter(this));
        adapter.register(ArticleGameList.class, new ArticleGameListAdapter(this));


        ArticleHeader articleHeader = new ArticleHeader(result.get(0));
        if ((int) result.get(0).get("page_size") != 1) {
            max_pages = (int) result.get(0).get("page_size");
            ArrayList<String> l = new ArrayList<>();
            for (int i = 1; i < max_pages + 1; i++) {
                l.add((String) result.get(0).get("page_" + String.valueOf(i)));
            }
            items.add(new MutilPages(l));
        }
        items.add(new Line());
        items.add(articleHeader);


        ArrayList<Map<String, Object>> game_list = (ArrayList<Map<String, Object>>) result.get(2).get("gamelist");
        if (game_list.size() != 0) {
            items.add(new Category("游戏"));
            items.add(new Line());
        }
        for (Map<String, Object> map : game_list) {
            ArticleGameList articleGameList = new ArticleGameList(map);
            items.add(articleGameList);
        }

        ArrayList<Map<String, Object>> replies_list = (ArrayList<Map<String, Object>>) result.get(1).get("list");
        items.add(new Category("回复"));
        items.add(new Line());
        for (Map<String, Object> map : replies_list) {
            ArticleReply articleReply = new ArticleReply(map);
            items.add(articleReply);
            items.add(new Line());
        }

        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
        if (max_pages != 1) {
            recyclerView.scrollToPosition(1);
        }
        if (form_reply) {
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            form_reply = false;
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onHeaderClick(View view) {
//        TODO
    }

    @Override
    public void onGameClick(View view) {
        Intent intent = new Intent(context, GameTrophyActivity.class);
        intent.putExtra("game_id", view.getTag().toString());
        startActivity(intent);
    }

    @Override
    public void onpagechage(int page) {
        current_page = page;
        recyclerView.scrollToPosition(1);
        getData();
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
        intent.putExtra("id", a_id);
        intent.putExtra("username", username);
        if (content != null) {
            intent.putExtra("content", content);
            intent.putExtra("comment_id", comment_id);
        }
        startActivityForResult(intent, 100);
    }
}
