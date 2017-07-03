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
import club.ranleng.psnine.adapter.ArticleGameListAdapter;
import club.ranleng.psnine.adapter.ArticleHeaderAdapter;
import club.ranleng.psnine.adapter.ArticleReplyAdapter;
import club.ranleng.psnine.adapter.TextItemAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.model.ArticleGameList;
import club.ranleng.psnine.model.ArticleHeader;
import club.ranleng.psnine.model.ArticleReply;
import club.ranleng.psnine.model.TextItem;
import club.ranleng.psnine.widget.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestPost;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import club.ranleng.psnine.widget.UserStatus;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import okhttp3.FormBody;

public class ArticleActivity extends BaseActivity
        implements RequestWebPageListener,SwipeRefreshLayout.OnRefreshListener, ArticleReplyAdapter.OnItemClickListener{

    private String type;
    private String id;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_article);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        id = intent.getStringExtra("id");
        setTitle("No. " + id);
        context = this;
        new RequestWebPage(type,id,this);
    }

    @Override
    public void showContent() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.article_menu, menu);
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
            startActivity(new Intent(this,ReplyActivity.class));
        }

        return super.onOptionsItemSelected(item);
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
        MakeToast.notfound(context);
        finish();
    }

    @Override
    public void onSuccess(ArrayList<Map<String, Object>> result) {
        MultiTypeAdapter adapter = new MultiTypeAdapter();
        Items items = new Items();

        adapter.register(ArticleReply.class, new ArticleReplyAdapter(this));
        adapter.register(ArticleHeader.class, new ArticleHeaderAdapter());
        adapter.register(ArticleGameList.class, new ArticleGameListAdapter());
        adapter.register(TextItem.class, new TextItemAdapter());
        /*

         */
        ArticleHeader articleHeader = new ArticleHeader(result.get(0));
        items.add(articleHeader);

        ArrayList<Map<String, Object>> game_list = (ArrayList<Map<String, Object>>) result.get(2).get("gamelist");
        if(game_list.size() != 0){
            items.add(new TextItem("游戏", "#B9C4CD",14));
        }
        for(Map<String, Object> map : game_list){
            ArticleGameList articleGameList = new ArticleGameList(map);
            items.add(articleGameList);
        }

        ArrayList<Map<String, Object>> replies_list = (ArrayList<Map<String, Object>>) result.get(1).get("list");
        items.add(new TextItem("回复", "#B9C4CD",14));
        for(Map<String, Object> map: replies_list ){
            ArticleReply articleReply = new ArticleReply(map);
            items.add(articleReply);
        }

        adapter.setItems(items);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);
    }

    private void showDialog(Boolean editable, final String comment_id, final String username){
        final String[] list;
        if(type.contentEquals("gene")){
            if(editable){
                list = new String[]{"回复", "修改", "查看用户"};
            }else{
                list = new String[]{"回复", "查看用户"};
            }
        }else{
            if(editable){
                list = new String[]{"回复", "修改", "顶", "查看用户"};
            }else{
                list = new String[]{"回复", "顶", "查看用户"};
            }
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (list[which]) {
                    case "回复":
                        if(!UserStatus.isLogin()){
                            MakeToast.plzlogin(context);
                            break;
                        }else{
                            //do sth
                        }
                        break;
                    case "修改":
//                        final EditText reply = new EditText(context);
//                        builder.setView(reply).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                new CommitAction.edit().execute("comment", comment_id, reply.getText().toString());
//                            }
//                        });
//
//                        builder.create().show();
                        break;
                    case "查看用户":
                        Intent intent = new Intent(context,  PersonInfoActivity.class);
                        intent.putExtra("psnid",username);
                        startActivity(intent);
                        break;
                    case "顶":
                        if(!UserStatus.isLogin()){
                            MakeToast.plzlogin(context);
                            break;
                        }else{
                            builder.setMessage("要付出4铜币来顶一下吗？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            FormBody body = new FormBody.Builder()
                                                    .add("type","comment")
                                                    .add("param",comment_id)
                                                    .add("updown","up")
                                                    .build();
                                            new RequestPost(context,"updown",body);
                                        }
                                    });
                            builder.create().show();
                        }
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(View view, int position) {
        showDialog((Boolean) view.getTag(R.id.tag_article_replies_editable),
                (String) view.getTag(R.id.tag_article_replies_id),
                (String) view.getTag(R.id.tag_article_replies_username));
    }
}
