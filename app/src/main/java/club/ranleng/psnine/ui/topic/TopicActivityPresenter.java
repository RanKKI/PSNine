package club.ranleng.psnine.ui.topic;

import club.ranleng.psnine.base.model.BaseTopic;
import club.ranleng.psnine.data.module.TopicCommentCallback;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.model.Topic.Topic;
import club.ranleng.psnine.model.Topic.TopicComment;
import club.ranleng.psnine.model.Topic.TopicGene;
import club.ranleng.psnine.utils.Parse;
import io.reactivex.functions.Consumer;

public class TopicActivityPresenter<T> implements TopicActivityContract.Presenter {

    private TopicActivityContract.View view;
    private TopicListAdapter adapter;
    private int comment_page = 1;
    private int maxCommentPage = 1;
    private String id;
    private Class<T> tClass;
    private boolean loadComment = true;

    TopicActivityPresenter(TopicActivityContract.View view, Class<T> tClass) {
        this.view = view;
        this.tClass = tClass;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        adapter = new TopicListAdapter(view.getContext());
        view.setupList(adapter);
        id = Parse.getID(view.getURL());
        loadComment = true;
        loadTopic();
    }

    @Override
    public void loadTopic() {
        view.loading(true);
        ApiManager.getDefault()
                .getTopic(view.getType(), id, tClass)
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        BaseTopic baseTopic = (BaseTopic) t;
                        adapter.setHeaderView(baseTopic);
                        view.setSubtitle(baseTopic.getTitle());
                        if (t instanceof TopicGene) {
                            TopicGene topicGene = (TopicGene) t;
                            view.setMenu(topicGene.getOriginalUrl());
                        }

                        if(t instanceof Topic){
                            adapter.setTopicGame(baseTopic.getGames());
                        }
                        if (loadComment) loadComment();
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
        loadComment = true;
        adapter.clear();
        loadTopic();
    }

    @Override
    public void submitComment(String content) {
        ApiManager.getDefault().setReply(new TopicCommentCallback() {
            @Override
            public void onSuccess(TopicComment.Comment comment) {
                view.setReplyContent("", true);
                view.setReplyLayout(false);
                adapter.addComment(comment);
            }

            @Override
            public void onFailure() {
            }
        }, view.getType(), id, content);
    }

}
