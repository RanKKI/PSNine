package club.ranleng.psnine.view;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;

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

    public void setAutoLoadListener(Fragment fragment){
        addOnScrollListener(new AutoLoadScrollListener(fragment));
    }

    public void setOnLoadMore(onLoadMoreListener listener){
        this.onLoadMoreListener = listener;
    }

    public interface onLoadMoreListener{
        void loadMore();
        boolean isLoading();
    }

    class AutoLoadScrollListener extends OnScrollListener {
        private Fragment fragment;

        AutoLoadScrollListener(Fragment fragment) {
            super();
            this.fragment = fragment;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(onLoadMoreListener == null){
                return;
            }
            if(onLoadMoreListener.isLoading()){
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
            switch (newState){
                case 0:
                    Glide.with(fragment).resumeRequests();
                    break;
                case 1:
                    Glide.with(fragment).pauseRequests();
                    break;
                case 2:
                    Glide.with(fragment).resumeRequests();
                    break;

            }
        }
    }
}