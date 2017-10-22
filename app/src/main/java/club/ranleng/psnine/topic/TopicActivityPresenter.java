package club.ranleng.psnine.topic;

import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.model.Topic;
import club.ranleng.psnine.model.TopicComment;
import io.reactivex.functions.Consumer;

public class TopicActivityPresenter implements TopicActivityContract.Presenter {

    private TopicActivityContract.View view;
    private int comment_page = 1;
    private TopicListAdapter adapter;

    public TopicActivityPresenter(TopicActivityContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        adapter = new TopicListAdapter(view.getContext());
        view.setupList(adapter);
        loadTopic();
    }

    @Override
    public void loadTopic() {
        ApiManager.getDefault().getTopic("32572")
                .subscribe(new Consumer<Topic>() {
                    @Override
                    public void accept(Topic topic) throws Exception {
                        adapter.setHeaderView(topic);
                    }
                });
    }

    @Override
    public void loadComment() {
        ApiManager.getDefault().getTopicComment("32572", comment_page)
                .subscribe(new Consumer<TopicComment>() {
                    @Override
                    public void accept(TopicComment topicComment) throws Exception {

                    }
                });
    }

}
