package club.ranleng.psnine.view;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;

import java.util.List;

import club.ranleng.psnine.R;

public class SmartRecyclerView extends RecyclerView {

    private onLoadMoreListener onLoadMoreListener;

    public SmartRecyclerView(Context context) {
        super(context);
    }

    public SmartRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAutoLoadListener(Context context) {
        addOnScrollListener(new ScrollListener(context));

    }

    public void setAutoLoadListener(Fragment fragment) {
        setAutoLoadListener(fragment.getActivity());
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


    public interface onLoadMoreListener {
        void loadMore();

        boolean isLoading();
    }


    class ScrollListener extends OnScrollListener {

        private Context context;

        ScrollListener(Context context) {
            this.context = context;
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
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    onLoadMoreListener.loadMore();
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

//            if (newState == SCROLL_STATE_IDLE || newState == SCROLL_STATE_DRAGGING) {
//                Glide.with(context).resumeRequests();
//            } else if (newState == SCROLL_STATE_SETTLING) {
//                Glide.with(context).pauseRequests();
//            }
        }
    }
}
