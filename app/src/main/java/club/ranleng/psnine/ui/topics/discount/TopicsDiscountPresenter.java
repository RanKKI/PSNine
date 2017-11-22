package club.ranleng.psnine.ui.topics.discount;

import club.ranleng.psnine.data.remote.ApiTopic;
import club.ranleng.psnine.model.Topics.TopicsDiscount;
import club.ranleng.psnine.ui.topics.base.TopicsContract;
import io.reactivex.functions.Consumer;

public class TopicsDiscountPresenter implements TopicsContract.Presenter {

    private TopicsContract.View view;
    private TopicsDiscountListAdapter adapter;
    private int page = 1;

    public TopicsDiscountPresenter(TopicsContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        adapter = new TopicsDiscountListAdapter(view.getFragment());
        view.setupList(adapter);
        load();
    }

    @Override
    public void load() {
        view.loading(true);
        new ApiTopic<TopicsDiscount>().getTopics(view.getType(), page, view.getQuery(), TopicsDiscount.class)
                .subscribe(new Consumer<TopicsDiscount>() {
                    @Override
                    public void accept(TopicsDiscount discount) throws Exception {
                        adapter.add(discount, page);
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
