package club.ranleng.psnine.ui.topics;

import club.ranleng.psnine.base.BaseTopics;
import club.ranleng.psnine.data.remote.getTopics;
import io.reactivex.functions.Consumer;

public class TopicsFragmentPresenter<T> implements TopicsFragmentContract.Presenter {

    private TopicsFragmentContract.View view;
    private TopicsFragmentListAdapter adapter;
    private int page = 1;
    private Class<T> tClass;

    TopicsFragmentPresenter(TopicsFragmentContract.View view, Class<T> tClass) {
        this.view = view;
        this.tClass = tClass;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        adapter = new TopicsFragmentListAdapter<BaseTopics.BaseItem>(view.getFragment());
        view.setupList(adapter);
        load();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void load() {
        view.loading(true);
        new getTopics<T>().getTopics(view.getType(), page, tClass)
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        adapter.add((BaseTopics<T>) t);
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
