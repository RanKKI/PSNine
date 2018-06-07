package xyz.rankki.psnine.common.listener

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import com.bumptech.glide.Glide
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RecyclerViewStopGlideWhenScrollListener(private val mContext: Context) : RecyclerView.OnScrollListener() {

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