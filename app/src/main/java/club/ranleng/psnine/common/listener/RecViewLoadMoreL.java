package club.ranleng.psnine.common.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;

public class RecViewLoadMoreL extends RecyclerView.OnScrollListener {

    private int lastPosition;
    private int lastItemCount;
    private int itemCount;
    private int current_page = 1;
    private int maxPage;

    private LinearLayoutManager mLayoutManager;
    private onScrollToBottomListener listener;

    public RecViewLoadMoreL(onScrollToBottomListener listener) {
        this.listener = listener;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);


        if (mLayoutManager == null) {
            mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        }
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            itemCount = mLayoutManager.getItemCount();
            lastPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
        } else {
            return;
        }
        if (lastItemCount != itemCount && lastPosition == itemCount - 1 && current_page < maxPage) {
            current_page++;
            lastItemCount = itemCount;
            if (listener != null) {
                listener.LoadMore(current_page);
            }
        }
    }

    public interface onScrollToBottomListener {
        void LoadMore(int page);
    }
}
