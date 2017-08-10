package club.ranleng.psnine.module.trophy;

import club.ranleng.psnine.common.multitype.binder.PSNGameHeaderBinder;
import club.ranleng.psnine.common.multitype.binder.PSNGameTrophyBinder;
import club.ranleng.psnine.common.multitype.binder.PSNGameUserBinder;
import club.ranleng.psnine.common.multitype.model.PSNGameHeader;
import club.ranleng.psnine.common.multitype.model.PSNGameTrophy;
import club.ranleng.psnine.common.multitype.model.PSNGameUser;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.CategoryViewBinder;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;

public class TrophyPresenter implements TrophyContract.Presenter {

    private TrophyContract.View mTrophyView;
    private Items items;
    private MultiTypeAdapter adapter;

    public TrophyPresenter(TrophyContract.View view) {
        this.mTrophyView = view;
        this.mTrophyView.setPresenter(this);

        adapter = new MultiTypeAdapter();
        adapter.register(PSNGameTrophy.class, new PSNGameTrophyBinder());
        adapter.register(PSNGameHeader.class, new PSNGameHeaderBinder());
        adapter.register(PSNGameUser.class, new PSNGameUserBinder());
        adapter.register(Line.class, new LineViewBinder());
        adapter.register(Category.class, new CategoryViewBinder());
        items = new Items();
        adapter.setItems(items);
    }

    @Override
    public void start() {

    }

    @Override
    public void load() {

    }
}
