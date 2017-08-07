package club.ranleng.psnine.module.element;

import android.view.View;

import com.blankj.utilcode.util.LogUtils;

import club.ranleng.psnine.common.multitype.binder.CardViewBinder;
import club.ranleng.psnine.common.multitype.model.CardBean;
import club.ranleng.psnine.data.moudle.SimpleSubCallBack;
import club.ranleng.psnine.data.remote.ApiManager;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.CategoryViewBinder;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;

public class ElementPresenter
        implements ElementContract.Presenter, View.OnClickListener, SimpleSubCallBack<String> {


    private ElementContract.View mElementView;
    private MultiTypeAdapter adapter = new MultiTypeAdapter();
    private Items items = new Items();

    public ElementPresenter(ElementContract.View view) {
        this.mElementView = view;
        this.mElementView.setPresenter(this);

        adapter.register(CardBean.class, new CardViewBinder(this));
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(Line.class, new LineViewBinder());
        adapter.setItems(items);
    }

    @Override
    public void start() {
        load();
    }

    @Override
    public void load() {
        items.clear();
        ApiManager.getDefault().getElement(this);
    }

    @Override
    public void onClick(View v) {
        mElementView.openTopics((String) v.getTag());
    }

    @Override
    public void onStart() {
        mElementView.setLoading(true);
        items.add(new Category("曾使用过的「元素」"));
    }

    @Override
    public void onNext(String s) {
        items.add(new CardBean(s));
    }

    @Override
    public void onComplete() {
        mElementView.setLoading(false);
        mElementView.setAdapter(adapter);
    }
}
