package club.ranleng.psnine.ui.topics.normal;

import club.ranleng.psnine.base.model.BaseTopics;
import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.ui.topics.base.TopicsContract;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class TopicsPresenter<T extends BaseTopics> implements TopicsContract.Presenter {

    private TopicsContract.View view;
    private TopicsListAdapter adapter;
    private int page = 1;
    private Class<T> tClass;

    public TopicsPresenter(TopicsContract.View view, Class<T> tClass) {
        this.view = view;
        this.tClass = tClass;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        adapter = new TopicsListAdapter<T.BaseItem>(view.getFragment());
        view.setupList(adapter);
        load();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void load() {
        view.loading(true);
        Observable<T> observable;
        if (view.getType() == Key.PSNTOPIC || view.getType() == Key.PSNGENE) {
            observable = ApiManager.getDefault().getPSN(view.getPSNID(), view.getType(), tClass);
        } else {
            observable = ApiManager.getDefault().getTopics(view.getType(), page, view.getQuery(), tClass);
        }
        observable.subscribe(new Consumer<T>() {
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

    @Override
    public boolean menuItemSelected(int id) {
        return false;
    }

}
