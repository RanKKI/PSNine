package club.ranleng.psnine.ui.topic;

import club.ranleng.psnine.base.BaseTopic;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.data.remote.ApiTopic;
import club.ranleng.psnine.model.TopicComment;
import club.ranleng.psnine.utils.ParseUrl;
import io.reactivex.functions.Consumer;

public class TopicActivityPresenter<T> implements TopicActivityContract.Presenter {

    private TopicActivityContract.View view;
    private TopicListAdapter adapter;
    private int comment_page = 1;
    private int maxCommentPage = 1;
    private String id;
    private Class<T> tClass;


    TopicActivityPresenter(TopicActivityContract.View view, Class<T> tClass) {
        this.view = view;
        this.tClass = tClass;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        adapter = new TopicListAdapter(view.getContext());
        view.setupList(adapter);
        String url = view.getURL();
        id = ParseUrl.getID(url);
        loadTopic();
    }

    @Override
    public void loadTopic() {
        view.loading(true);
        new ApiTopic<T>().getTopic(view.getType(), id, tClass).subscribe(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                BaseTopic baseTopic = (BaseTopic) t;
                adapter.setHeaderView(baseTopic);
                String sub = baseTopic.getContent();
                view.setSubtitle(sub.length() > 20 ? sub.substring(0, 20) : sub);
                loadComment();
            }
        });
    }

    @Override
    public void loadComment() {
        view.loading(true);
        ApiManager.getDefault().getTopicComment(view.getType(), id, comment_page)
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
