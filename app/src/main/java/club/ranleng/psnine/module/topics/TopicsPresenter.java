package club.ranleng.psnine.module.topics;


import android.view.View;

import com.blankj.utilcode.util.LogUtils;

import java.util.List;

import club.ranleng.psnine.bean.Topic;
import club.ranleng.psnine.bean.Topics;
import club.ranleng.psnine.common.multitype.binder.TopicsListBinder;
import club.ranleng.psnine.common.multitype.model.MaxPages;
import club.ranleng.psnine.common.multitype.model.TopicsBean;
import club.ranleng.psnine.data.moudle.SimpleReturn;
import club.ranleng.psnine.data.remote.ApiManager;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;

public class TopicsPresenter implements TopicsContract.Presenter, SimpleReturn<Items> {

    private final TopicsContract.View mTopicsView;
    private MultiTypeAdapter adapter;
    private Topics topics;

    public TopicsPresenter(TopicsContract.View view) {
        mTopicsView = view;
        mTopicsView.setPresenter(this);

        adapter = new MultiTypeAdapter();
        adapter.register(Line.class, new LineViewBinder());
        adapter.register(TopicsBean.class, new TopicsListBinder(new TopicsListBinder.TopicsListListener() {
            @Override
            public void onClick(int type, int topic_id, View icon) {
                Topic topic = new Topic(topic_id, type);
                mTopicsView.openTopic(topic, icon);
            }
        }));
        adapter.setItems(new Items());
    }

    @Override
    public void start() {
        mTopicsView.showTopics(adapter);
        topics = mTopicsView.getTopicsInfo();
        loadTopics();
    }

    @Override
    public void loadTopics() {
        mTopicsView.setCurrent_page(1);
        loadTopics(1);
    }

    @Override
    public void loadTopics(int page) {
        LogUtils.d("load page: " + page);
        mTopicsView.setCurrent_page(page);
        mTopicsView.showLoading(true);
        ApiManager.getDefault().getTopics(this, topics.getType(), topics.getEle(), topics.getQuery(), page);
    }

    @Override
    public void accept(final Items objects) {
        int max = ((MaxPages) objects.get(0)).getMaxPages();
        LogUtils.d("set max pages: " + max);
        mTopicsView.setMaxPage(max);
        topics.setMax_page(max);
        objects.remove(0);
        int end = adapter.getItemCount();
        if (topics.getPage() == 1) {
            adapter.setItems(objects);
            adapter.notifyItemChanged(0, objects.size());
        } else {
            adapter.getItems().addAll((List) objects);
            adapter.notifyItemRangeInserted(end, objects.size());
        }
        mTopicsView.showLoading(false);
    }
}
