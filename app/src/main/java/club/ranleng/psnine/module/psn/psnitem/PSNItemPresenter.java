package club.ranleng.psnine.module.psn.psnitem;

import java.util.Map;

import club.ranleng.psnine.bean.Topic;
import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.common.multitype.binder.ArticleReplyBinder;
import club.ranleng.psnine.common.multitype.binder.PSNGameListBinder;
import club.ranleng.psnine.common.multitype.binder.TopicsListBinder;
import club.ranleng.psnine.common.multitype.model.ArticleReply;
import club.ranleng.psnine.common.multitype.model.GameList;
import club.ranleng.psnine.common.multitype.model.TopicsBean;
import club.ranleng.psnine.data.moudle.SimpleSubCallBack;
import club.ranleng.psnine.data.remote.ApiManager;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.CategoryViewBinder;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;

public class PSNItemPresenter implements PSNItemContract.Presenter, SimpleSubCallBack<Map<String, Object>> {

    private PSNItemContract.View mPSNView;
    private Items items;
    private MultiTypeAdapter adapter;
    private String psnid;
    private int type;

    public PSNItemPresenter(PSNItemContract.View view) {
        this.mPSNView = view;
        this.mPSNView.setPresenter(this);
        adapter = new MultiTypeAdapter();
        items = new Items();
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(Line.class, new LineViewBinder());
        adapter.register(TopicsBean.class, new TopicsListBinder(new TopicsListBinder.TopicsListListener() {
            @Override
            public void onClick(int type, int topic_id) {
                Topic topic = new Topic(topic_id, type);
                mPSNView.openTopic(topic);
            }
        }));
        adapter.register(ArticleReply.class, new ArticleReplyBinder());
        adapter.register(GameList.class, new PSNGameListBinder(psnid));
        adapter.setItems(items);
    }

    @Override
    public void start() {
        psnid = mPSNView.getPSNID();
        type = mPSNView.getType();
        load();
    }

    @Override
    public void load() {
        ApiManager.getDefault().getPSN(this, psnid, type);
    }

    @Override
    public void onStart() {
        mPSNView.Loading(true);
    }

    @Override
    public void onNext(Map<String, Object> map) {
        if (map.containsKey("max_page")) {
            return;
        }
        if (type == KEY.GENE || type == KEY.TOPIC) {
            items.add(new TopicsBean(map));
        } else if (type == KEY.PSN_GAME) {
            items.add(new GameList(map));
        } else if (type == KEY.PSN_MSG) {
            items.add(new ArticleReply(map));
        }
        items.add(new Line());
    }

    @Override
    public void onComplete() {
        mPSNView.setAdapter(adapter);
        mPSNView.Loading(false);
    }
}
