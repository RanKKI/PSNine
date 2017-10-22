package club.ranleng.psnine.topics;

import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.model.TopicsNormal;
import io.reactivex.functions.Consumer;

public class TopicsFragmentPresenter implements TopicsFragmentContract.Presenter {

    private TopicsFragmentContract.View view;
    private TopicsListAdapter adapter;
    private int page = 1;

    TopicsFragmentPresenter(TopicsFragmentContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        adapter = new TopicsListAdapter(view.getFragment());
        view.setupList(adapter);
        load();
    }

    @Override
    public void load() {
        view.loading(true);
        ApiManager.getDefault().getTopics(0, page)
                .subscribe(new Consumer<TopicsNormal>() {
                    @Override
                    public void accept(TopicsNormal topicsNormal) throws Exception {
                        adapter.add(topicsNormal);
                        view.loading(false);
                    }
                });
    }

    @Override
    public void loadMore() {
        page++;
        load();
    }

    @Override
    public void clean() {
        page = 1;
        adapter.clear();
    }
}
