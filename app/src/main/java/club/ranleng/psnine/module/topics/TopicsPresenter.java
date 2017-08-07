package club.ranleng.psnine.module.topics;


import java.util.Map;

import club.ranleng.psnine.bean.Topic;
import club.ranleng.psnine.bean.Topics;
import club.ranleng.psnine.common.multitype.binder.TopicsListBinder;
import club.ranleng.psnine.common.multitype.model.TopicsBean;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.data.moudle.SimpleSubCallBack;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;

public class TopicsPresenter implements TopicsContract.Presenter, SimpleSubCallBack<Map<String, Object>> {

    private final TopicsContract.View mTopicsView;
    private Items items;
    private MultiTypeAdapter adapter;
    private Topics topics;

    public TopicsPresenter(TopicsContract.View view) {
        mTopicsView = view;
        mTopicsView.setPresenter(this);

        adapter = new MultiTypeAdapter();
        adapter.register(Line.class, new LineViewBinder());
        adapter.register(TopicsBean.class, new TopicsListBinder(new TopicsListBinder.TopicsListListener() {
            @Override
            public void onClick(int type, int topic_id) {
                Topic topic = new Topic(topic_id, type);
                mTopicsView.openTopic(topic);
            }
        }));
        items = new Items();
        adapter.setItems(items);
    }

    @Override
    public void start() {
        topics = mTopicsView.getTopicsInfo();
        loadTopics();
    }

    @Override
    public void loadTopics() {
        items.clear();
        mTopicsView.setCurrent_page(1);
        loadTopics(1);
    }

    @Override
    public void loadTopics(int page) {
        ApiManager.getDefault().getTopics(this, topics.getType(), topics.getEle(), topics.getQuery(), page);
    }

    @Override
    public void onStart() {
        mTopicsView.showLoading(true);
    }

    @Override
    public void onNext(Map<String, Object> map) {
        if (map.containsKey("max_page")) {
            mTopicsView.setMaxPage((int) map.get("max_page"));
            return;
        }
        items.add(new TopicsBean(map));
        items.add(new Line());
        adapter.notifyItemInserted(items.size());
    }

    @Override
    public void onComplete() {
        mTopicsView.showTopics(adapter);
        mTopicsView.showLoading(false);
    }
}
