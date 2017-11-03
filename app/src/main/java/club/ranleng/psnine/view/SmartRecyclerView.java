package club.ranleng.psnine.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;

import club.ranleng.psnine.R;

public class SmartRecyclerView<T> extends RecyclerView {

    private onLoadMoreListener onLoadMoreListener;
    private onMoving onMoving;

    public SmartRecyclerView(Context context) {
        super(context);
    }

    public SmartRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAutoLoadListener(T t) {
        addOnScrollListener(new ScrollListener<>(t));
    }

    public void setOnLoadMore(onLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    public void setDivider() {
        Drawable d_divider = getContext().getDrawable(R.drawable.recyclerview_divider);
        if (d_divider != null) {
            DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            divider.setDrawable(d_divider);
            addItemDecoration(divider);
        }
    }

    public void setOnMoving(onMoving onMoving) {
        this.onMoving = onMoving;
    }

    public interface onLoadMoreListener {
        void loadMore();

        boolean isLoading();
    }

    public interface onMoving {
        void onScroll();

        void onStop();

        boolean isReplyLayoutShowing();
    }

    class ScrollListener<K> extends OnScrollListener {

        private Fragment fragment;
        private Activity activity;

        ScrollListener(K k) {
            if (k instanceof Fragment) {
                fragment = (Fragment) k;
            } else if (k instanceof Activity) {
                activity = (Activity) k;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (onLoadMoreListener == null) {
                return;
            }
            if (onLoadMoreListener.isLoading()) {
                return;
            }
            if (getLayoutManager() instanceof LinearLayoutManager) {
                int lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = SmartRecyclerView.this.getAdapter().getItemCount();
                if (lastVisibleItem >= totalItemCount - 2 && dy > 0) {
                    onLoadMoreListener.loadMore();
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == SCROLL_STATE_IDLE) {
                if (onMoving != null && !onMoving.isReplyLayoutShowing()) {
                    onMoving.onStop();
                }
                if (activity != null && !activity.isDestroyed()) {
                    Glide.with(activity).resumeRequests();
                } else if (fragment != null && !fragment.isDetached()) {
                    Glide.with(fragment).resumeRequests();
                }
            } else if (newState == SCROLL_STATE_DRAGGING || newState == SCROLL_STATE_SETTLING) {
                if (onMoving != null && !onMoving.isReplyLayoutShowing()) {
                    onMoving.onScroll();
                }
                if (activity != null && !activity.isDestroyed()) {
                    Glide.with(activity).pauseRequests();
                } else if (fragment != null && !fragment.isDetached()) {
                    Glide.with(fragment).pauseRequests();
                }
            }
        }
    }
}
