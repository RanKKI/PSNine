package club.ranleng.psnine.ui.topics.psngame;

import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.model.PSNGames;
import club.ranleng.psnine.ui.topics.base.TopicsContract;
import io.reactivex.functions.Consumer;

public class TopicsPSNGamePresenter implements TopicsContract.Presenter {

    private TopicsContract.View view;
    private int page = 1;
    private TopicsPSNGameListAdapter adapter;

    public TopicsPSNGamePresenter(TopicsContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        adapter = new TopicsPSNGameListAdapter(view.getFragment());
        view.setupList(adapter);
        load();
    }

    @Override
    public void load() {
        view.loading(true);
        ApiManager.getDefault().getPSN(view.getPSNID(), Key.PSNGAMES, PSNGames.class)
                .subscribe(new Consumer<PSNGames>() {
                    @Override
                    public void accept(PSNGames psnGames) throws Exception {
                        adapter.add(psnGames.getItems(), page);
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
