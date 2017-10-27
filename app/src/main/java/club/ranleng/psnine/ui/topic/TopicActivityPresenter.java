package club.ranleng.psnine.ui.topic;

import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.model.Topic;
import club.ranleng.psnine.model.TopicComment;
import io.reactivex.functions.Consumer;

public class TopicActivityPresenter implements TopicActivityContract.Presenter {

    private TopicActivityContract.View view;
    private int comment_page = 1;
    private TopicListAdapter adapter;
    private String id;
    private int maxCommentPage = 1;

    TopicActivityPresenter(TopicActivityContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        adapter = new TopicListAdapter(view.getContext());
        view.setupList(adapter);
        String url = view.getURL();
        if (url.startsWith("http://psnine.com/topic/")) {
            id = url.replace("http://psnine.com/topic/", "");
        } else {
            id = url;
        }
        loadTopic();
    }

    @Override
    public void loadTopic() {
        view.loading(true);
        ApiManager.getDefault().getTopic(id)
                .subscribe(new Consumer<Topic>() {
                    @Override
                    public void accept(Topic topic) throws Exception {
                        adapter.setHeaderView(topic);
                        view.setSubtitle(topic.getContent());
                        loadComment();
                    }
                });
    }

    @Override
    public void loadComment() {
        view.loading(true);
        ApiManager.getDefault().getTopicComment(id, comment_page)
                .subscribe(new Consumer<TopicComment>() {
                    @Override
                    public void accept(TopicComment topicComment) throws Exception {
                        adapter.addComments(topicComment);
                        maxCommentPage = topicComment.getMaxPage();
                        view.loading(false);
                    }
                });
    }

    @Override
    public void loadMoreComment() {
        if (comment_page < maxCommentPage) {
            comment_page++;
            loadComment();
        }
    }

    @Override
    public void refresh() {
        comment_page = 1;
        adapter.clearComments();
        loadTopic();
    }

}
