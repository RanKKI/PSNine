package club.ranleng.psnine.ui.newTopic;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.utils.Parse;
import club.ranleng.psnine.utils.TextUtils;
import okhttp3.FormBody;

public class newTopicFragment extends BaseFragment implements newTopicContact.View {

    @BindView(R.id.swipelayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.new_topic_content) EditText content;
    @BindView(R.id.new_topic_title) EditText title;
    @BindView(R.id.new_topic_waning_top) TextView waning_top;

    private Context context;
    private newTopicContact.Presenter presenter;
    private int type;
    private int open = 1;

    public static newTopicFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        newTopicFragment fragment = new newTopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.layout_new_topic, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        type = getArguments().getInt("type");
        newTopicPresenter.newInstance(this);
        presenter.start();
    }

    @OnClick(R.id.new_topic_submit)
    public void newGeneSubmit() {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("open", TextUtils.toString(open))
                .add("title", TextUtils.toString(title))
                .add("content", TextUtils.toString(content))
                .add("node", Parse.parseNodeForNewTopic(type));
        bodyBuilder.add("addtopic", "");
        presenter.post(bodyBuilder.build());
    }

    @Override
    public void setPresenter(newTopicContact.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setup() {
        getActivity().setTitle("发贴");
        String waning_a = "提问题请发到「<font color='blue'>问答</font>」板块，闲聊请发到「<font color='blue'>机因</font>」，否则将被关闭 + <font color='red'>扣分处理</font>";
        waning_top.setText(Html.fromHtml(waning_a));
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void finishPosted() {
        getActivity().finish();
    }

    @Override
    public Context getCtx() {
        return context;
    }
}
