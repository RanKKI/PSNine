package club.ranleng.psnine.module.newtopic.Topic;

import club.ranleng.psnine.data.moudle.SimpleCallBack;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.module.newtopic.Gene.newGeneContract;

public class newTopicPresenter implements newTopicContract.Presenter {

    private newTopicContract.View mNewTopicView;

    public newTopicPresenter(newTopicContract.View view) {
        this.mNewTopicView = view;
        this.mNewTopicView.setPresenter(this);
    }

    @Override
    public void start() {
        mNewTopicView.setup();
    }

    @Override
    public void submit() {
        ApiManager.getDefault().newTopic(new SimpleCallBack() {
            @Override
            public void Success() {
                mNewTopicView.finish();
            }

            @Override
            public void Failed() {

            }
        },mNewTopicView.getData());
    }
}
