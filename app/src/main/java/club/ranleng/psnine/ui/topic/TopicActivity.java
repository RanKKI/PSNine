package club.ranleng.psnine.ui.topic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.common.UserState;
import club.ranleng.psnine.model.Topic.Topic;
import club.ranleng.psnine.model.Topic.TopicGene;
import club.ranleng.psnine.utils.Parse;
import club.ranleng.psnine.view.SmartRecyclerView;

public class TopicActivity extends BaseActivity implements TopicActivityContract.View {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView) SmartRecyclerView recyclerView;
    @BindView(R.id.reply_root) RelativeLayout reply_root;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.content) EditText content;

    private TopicActivityContract.Presenter presenter;
    private String url;
    private int type;
    private String OriginalUrl;
    private long lastClickTime = 0;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_topic);
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("主题");
        url = getIntent().getStringExtra("url");
        type = Parse.getType(url);
        String title = getIntent().getStringExtra("content");
        if (title != null) toolbar.setSubtitle(title);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < 200) {
                    recyclerView.smoothScrollToPosition(0);
                }
                lastClickTime = clickTime;
            }
        });
    }

    @Override
    public void getData() {
        if (type == Key.GENE) {
            new TopicActivityPresenter<>(this, TopicGene.class);
        } else if (type == Key.QA) {
            //TODO
            ToastUtils.showShort(R.string.not_support);
            finish();
            return;
//            new TopicActivityPresenter<>(this, TopicQA.class);
        } else {
            new TopicActivityPresenter<>(this, Topic.class);
        }
        presenter.start();
        fab.setVisibility(UserState.isLogin() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_topic, menu);
        menu.findItem(R.id.activity_topic_menu_originalUrl).setVisible(OriginalUrl != null);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_topic_menu_originalUrl:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(OriginalUrl));
                startActivity(intent);
                return true;
            case R.id.activity_topic_menu_share:
                String shareContent = toolbar.getSubtitle() + " (分享自 @PSN中文站）" + url;
                ActivityUtils.startActivity(IntentUtils.getShareTextIntent(shareContent));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(TopicActivityContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setupList(TopicListAdapter adapter) {
        recyclerView.setDivider();
        recyclerView.setAutoLoadListener(this);
        recyclerView.setOnLoadMore(new SmartRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
                presenter.loadMoreComment();
            }

            @Override
            public boolean isLoading() {
                return refreshLayout.isRefreshing();
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loading(boolean loading) {
        refreshLayout.setRefreshing(loading);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public void setSubtitle(String subtitle) {
        if (toolbar.getSubtitle() == null) {
            toolbar.setSubtitle(subtitle);
        }
    }

    @Override
    public void setReplyLayout(boolean opening) {
        ani(reply_root, opening, false);
        ani(fab, !opening, true);
        if (!opening) {
            KeyboardUtils.hideSoftInput(this);
        }
    }

    @Override
    public void setReplyContent(String content, boolean clean) {
        if (clean) {
            this.content.setText(content);
        } else {
            this.content.append(content);
        }
    }

    @Override
    public void setMenu(String OriginalUrl) {
        this.OriginalUrl = OriginalUrl;
        invalidateOptionsMenu();
    }

    @OnClick(R.id.fab)
    public void openReplyLayout() {
        if (!UserState.isLogin()) {
            return;
        }
        setReplyLayout(true);
    }

    @OnClick(R.id.send_button)
    public void newComment() {
        if (!UserState.isLogin()) {
            return;
        }
        presenter.submitComment(content.getText().toString());
    }

    @Override
    public void onBackPressed() {
        if (reply_root.getVisibility() == View.VISIBLE) {
            setReplyLayout(false);
        } else {
            super.onBackPressed();
        }
    }

    private void ani(final View view, boolean show, boolean center) {
        int cx = view.getMeasuredWidth();
        int cy = view.getMeasuredHeight();
        int w = view.getWidth();
        int h = view.getHeight();
        float r = (float) Math.sqrt(w * w + h * h);
        if (center) {
            cx = cx / 2;
            cy = cy / 2;
            r = Math.max(w / 2, h / 2);
        }
        Animator anim;
        if (show) {
            anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, r);
            view.setVisibility(View.VISIBLE);
        } else {
            anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, r, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });
        }
        anim.start();
    }

    @Override
    public int getType() {
        return type;
    }

}
