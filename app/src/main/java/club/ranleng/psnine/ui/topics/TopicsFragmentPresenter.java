package club.ranleng.psnine.ui.topics;

import club.ranleng.psnine.base.model.BaseTopics;
import club.ranleng.psnine.data.remote.ApiTopic;
import io.reactivex.functions.Consumer;

public class TopicsFragmentPresenter<T extends BaseTopics>
        implements TopicsFragmentContract.Presenter {

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
        adapter = new TopicsFragmentListAdapter<T.BaseItem>(view.getFragment());
        view.setupList(adapter);
        load();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void load() {
        view.loading(true);
        new ApiTopic<T>().getTopics(view.getType(), page, view.getQuery(), tClass)
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        adapter.add(t, page);
                        view.loading(false);
                        if (page == 1) {
                            view.scrollTo(0);
                        }
                    }
                });
    }

    @Override
    public void loadMore() {
        page++;
        load();
    }

    @Override
    public void refresh() {
        page = 1;
        load();
    }

}
