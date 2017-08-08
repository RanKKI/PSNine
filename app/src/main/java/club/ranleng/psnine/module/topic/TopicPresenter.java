package club.ranleng.psnine.module.topic;

import android.view.Menu;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.Map;

import club.ranleng.psnine.R;
import club.ranleng.psnine.bean.Topic;
import club.ranleng.psnine.common.multitype.binder.ArticleGameListBinder;
import club.ranleng.psnine.common.multitype.binder.ArticleHeaderBinder;
import club.ranleng.psnine.common.multitype.binder.ArticleReplyBinder;
import club.ranleng.psnine.common.multitype.binder.ImageBinder;
import club.ranleng.psnine.common.multitype.binder.MutilPagesBinder;
import club.ranleng.psnine.common.multitype.binder.TextEditableItemBinder;
import club.ranleng.psnine.common.multitype.model.ArticleGameList;
import club.ranleng.psnine.common.multitype.model.ArticleHeader;
import club.ranleng.psnine.common.multitype.model.ArticleReply;
import club.ranleng.psnine.common.multitype.model.Image_Gene;
import club.ranleng.psnine.common.multitype.model.MutilPages;
import club.ranleng.psnine.common.multitype.model.TextSpannedItem;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.data.moudle.SimpleSubCallBack;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.CategoryViewBinder;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;

public class TopicPresenter implements TopicContract.Presenter, SimpleSubCallBack<Map<String, Object>> {

    private TopicContract.View mTopicView;
    private Items items;
    private MultiTypeAdapter adapter;
    private Topic topic;
    private Boolean f_game = true;
    private Boolean f_reply = true;

    public TopicPresenter(TopicContract.View view) {
        this.mTopicView = view;
        this.mTopicView.setPresenter(this);

        adapter = new MultiTypeAdapter();
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

            }
        }));
        items = new Items();
        adapter.setItems(items);
    }

    @Override
    public void start() {
        topic = mTopicView.getTopic();
        loadTopic();
    }

    @Override
    public void reFresh() {

    }

    @Override
    public void loadTopic() {
        mTopicView.showLoading(true);
        ApiManager.getDefault().getTopic(this, topic.getType(), topic.getTopic_id(), topic.getPage());
    }

    @Override
    public void loadTopic(int page) {
        items.clear();
        topic.setPage(page);
        loadTopic();
    }

    @Override
    public void setMenu(Menu menu) {

    }

    @Override
    public void MenuSelected(int id) {

    }

    @Override
    public void hidePanel() {
        mTopicView.hidePanel();
    }

    @Override
    public void onContextMenu(int id, int position) {
        ArticleReply articleReply = (ArticleReply) items.get(position);
        switch (id){
            case R.id.adapter_reply_menu_edit:
                break;
            case R.id.adapter_reply_menu_reply:
                at(articleReply.username);
                break;
            case R.id.adapter_reply_menu_up:
                break;
            case R.id.adapter_reply_menu_user:
                break;
        }
        articleReply = null;
    }

    @Override
    public void at(String username) {
        mTopicView.addReply(String.format("@%s ",username));
    }

    @Override
    public void onStart() {
        mTopicView.showLoading(true);
    }

    @Override
    public void onNext(Map<String, Object> map) {
        if (map.containsKey("max_page")) {
            return;
        }
        Object map_type = map.get("type");
        if (map_type.equals("header")) {

            int max_pages = (int) map.get("page_size");

            if (max_pages != 1) {
                ArrayList<String> l = new ArrayList<>();
                for (int i = 1; i < max_pages + 1; i++) {
                    l.add((String) map.get("page_" + String.valueOf(i)));
                }
                items.add(new MutilPages(l));
                items.add(new Line());
            }

            topic.setEditable((Boolean) map.get("editable"));
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

            topic.setOriginal((String) map.get("original"));

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
    public void onComplete() {
        mTopicView.showLoading(false);
        mTopicView.showTopic(adapter);
        mTopicView.setMenu(topic);
    }
}
