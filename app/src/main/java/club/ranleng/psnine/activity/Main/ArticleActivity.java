package club.ranleng.psnine.activity.Main;

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
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import club.ranleng.psnine.Listener.ReplyPostListener;
import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.Assist.FragActivity;
import club.ranleng.psnine.activity.Post.NewGeneActivity;
import club.ranleng.psnine.activity.Post.NewTopicActivity;
import club.ranleng.psnine.activity.Post.ReplyActivity;
import club.ranleng.psnine.adapter.ViewBinder.Article.ArticleGameListBinder;
import club.ranleng.psnine.adapter.ViewBinder.Article.ArticleHeaderBinder;
import club.ranleng.psnine.adapter.ViewBinder.Article.ArticleReplyBinder;
import club.ranleng.psnine.adapter.ViewBinder.Common.ImageBinder;
import club.ranleng.psnine.adapter.ViewBinder.Common.MutilPagesBinder;
import club.ranleng.psnine.adapter.ViewBinder.TextEditableItemBinder;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.model.Article.ArticleGameList;
import club.ranleng.psnine.model.Article.ArticleHeader;
import club.ranleng.psnine.model.Article.ArticleReply;
import club.ranleng.psnine.model.Article.MutilPages;
import club.ranleng.psnine.model.Common.Image;
import club.ranleng.psnine.model.KEY;
import club.ranleng.psnine.model.TextSpannedItem;
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
        ArticleGameListBinder.OnItemClickListener, MutilPagesBinder.OnPageChange, ReplyPostListener {

    private static Context context;
    private String type;
    private String a_id;
    private int current_page = 1;
    private int max_pages = 1;
    private Boolean form_reply = false;
    private Boolean editable = false;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Items items;
    private LinearLayoutManager mLayoutManager;
    private String original = null;

    @Override
    public void setContentView() {
        setContentView(R.layout.toolbar_recyclerview);
        context = this;
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
        mLayoutManager = new LinearLayoutManager(this,
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
        menu.clear();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_article, menu);

        if (type.contentEquals("gene")) {
            menu.findItem(R.id.action_artivle_up).setVisible(false);
        }

        if(editable){
            menu.findItem(R.id.action_article_edit).setVisible(true);
        }

        if(original != null){
            menu.findItem(R.id.action_article_original).setVisible(true);
        }else {
            menu.findItem(R.id.action_article_original).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (!UserStatus.isLogin()) {
            MakeToast.plzlogin();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_article_reply) {
            Replies();
        } else if (id == R.id.action_artivle_fav) {
            FormBody body = new FormBody.Builder().add("type", type).add("param", a_id).build();
            new RequestPost(this, context, "fav", body);
        } else if (id == R.id.action_artivle_up) {
            FormBody body = new FormBody.Builder().add("type", type).add("param", a_id).add("updown", "up").build();
            new RequestPost(this, context, "updown", body);
        } else if (id ==R.id.action_article_original){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(original)));
        }else if( id == R.id.action_article_edit){
            if(type.contentEquals("gene")){
                Intent intent = new Intent(this, NewGeneActivity.class);
                intent.putExtra("editable",editable);
                intent.putExtra("topic_id",a_id);
                startActivity(intent);
            }else{
                Intent intent = new Intent(this, NewTopicActivity.class);
                intent.putExtra("editable",editable);
                intent.putExtra("topic_id",a_id);
                startActivity(intent);
            }

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
        adapter.register(Image.class, new ImageBinder());
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(MutilPages.class, new MutilPagesBinder(this));
        adapter.register(ArticleReply.class, new ArticleReplyBinder());
        adapter.register(ArticleHeader.class, new ArticleHeaderBinder());
        adapter.register(TextSpannedItem.class, new TextEditableItemBinder());
        adapter.register(ArticleGameList.class, new ArticleGameListBinder(this));


        Map<String, Object> header = result.get(0);
        ArticleHeader articleHeader = new ArticleHeader(header);
        max_pages = (int) header.get("page_size");
        if (max_pages != 1) {
            ArrayList<String> l = new ArrayList<>();
            for (int i = 1; i < max_pages + 1; i++) {
                l.add((String) header.get("page_" + String.valueOf(i)));
            }
            items.add(new MutilPages(l));
        }
        editable = (Boolean) header.get("editable");
        items.add(articleHeader);
        items.add(new Line());

        for (int i = 0; i < (int) header.get("img_size"); i++) {
            String url = (String) header.get("img_" + String.valueOf(i));
            items.add(new Image(url));
            items.add(new Line());
        }

        if(header.get("video") != null){
            items.add(new TextSpannedItem(String.format("<a href=\"%s\">打开视频链接</a>",header.get("video"))));
            items.add(new Line());
        }

        if(header.get("original") != null){
            original = (String) header.get("original");
        }

        ArrayList<Map<String, Object>> game_list = (ArrayList<Map<String, Object>>) result.get(2).get("gamelist");
        if (game_list.size() != 0) {
            items.add(new Category("游戏"));
            items.add(new Line());
        }
        for (Map<String, Object> map : game_list) {
            ArticleGameList articleGameList = new ArticleGameList(map);
            items.add(articleGameList);
            items.add(new Line());
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
            recyclerView.scrollToPosition(mLayoutManager.findLastVisibleItemPosition());
            form_reply = false;
        }
        invalidateOptionsMenu();
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onGameClick(View view) {
        Intent intent = new Intent(context, FragActivity.class);
        intent.putExtra("key", KEY.TROPHY);
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

    @Override
    public void ReplyPostFinish() {
        MakeToast.str("成功");
    }
}
