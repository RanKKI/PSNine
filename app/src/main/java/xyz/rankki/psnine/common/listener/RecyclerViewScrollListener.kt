package xyz.rankki.psnine.common.listener

import android.app.Activity
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import com.bumptech.glide.Glide
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RecyclerViewScrollListener(private val mContext: Context, private val loadingListener: LoadingListener?) : RecyclerView.OnScrollListener() {

    interface LoadingListener {

        fun loadMore()

        fun isLoading(): Boolean

    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (loadingListener == null || recyclerView == null || loadingListener.isLoading()) {
            return
        } else if (recyclerView.layoutManager is LinearLayoutManager) {
            val lastVisibleItemPosition: Int = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            val totalItems = recyclerView.adapter.itemCount
            if (lastVisibleItemPosition > totalItems - 4 && dy > 0) {
                loadingListener.loadMore()
            }
        }

    }

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        doAsync {
            if (!(mContext as Activity).isDestroyed) {
                uiThread {
                    when (newState) {
                        SCROLL_STATE_IDLE -> Glide.with(mContext).resumeRequests()
                        else -> Glide.with(mContext).pauseRequests()
                    }
                }
            }
        }
        super.onScrollStateChanged(recyclerView, newState)
    }

}