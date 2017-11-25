package club.ranleng.psnine.ui.topics.discount;

import com.blankj.utilcode.util.LogUtils;

import club.ranleng.psnine.R;
import club.ranleng.psnine.data.remote.ApiManager;
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
        view.setMenu(R.menu.fragment_topics_discount);
    }

    @Override
    public void load() {
        view.loading(true);
        ApiManager.getDefault().getTopicsDiscount("", "", "", "")
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

    @Override
    public boolean menuItemSelected(int id) {
        if (id == R.id.fragmentTopicsDiscountOptions) {
            LogUtils.d("options menu item clicked");
        }
        return false;
    }
}
