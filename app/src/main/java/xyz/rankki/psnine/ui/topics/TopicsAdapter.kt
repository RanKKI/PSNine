@file:Suppress("UNCHECKED_CAST")

package xyz.rankki.psnine.ui.topics

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.jetbrains.anko.*
import xyz.rankki.psnine.base.BaseTopicsModel
import xyz.rankki.psnine.ui.anko.TopicsItemUI

class TopicsAdapter<T>(private val mContext: Context) : RecyclerView.Adapter<TopicsAdapter.ViewHolder>() {

    private val ankoContext = AnkoContext.createReusable(mContext, this)

    private var data: ArrayList<T> = ArrayList()

    private var clickListener: View.OnClickListener? = null

    fun updateData(newData: ArrayList<*>) {
        doAsync {
            val start: Int = data.size - 1
            data.addAll(newData as Collection<T>)
            val end: Int = data.size
            uiThread {
                notifyItemRangeChanged(start, end)
            }
        }
    }

    fun setOnClickListener(listener: View.OnClickListener?) {
        clickListener = listener
    }

    fun getData(position: Int): BaseTopicsModel.BaseItem = data[position] as BaseTopicsModel.BaseItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = TopicsItemUI<TopicsAdapter<T>>().createView(ankoContext)
        view.setOnClickListener(clickListener)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = data[position] as BaseTopicsModel.BaseItem
        holder.tvTitle.text = topic.getTitle()
        holder.tvUsername.text = topic.getUsername()
        holder.tvTime.text = topic.getTime()
        holder.tvReplySize.text = topic.getReplySize()
        doAsync {
            val avatar = Glide.with(mContext)
                    .load(topic.getAvatar())
                    .submit(100, 100).get()
            uiThread {
                holder.ivAvatar.image = avatar
            }
        }
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        val tvTitle: TextView = view.find(TopicsItemUI.ID_Title)
        val tvUsername: TextView = view.find(TopicsItemUI.ID_Username)
        val tvTime: TextView = view.find(TopicsItemUI.ID_Time)
        val tvReplySize: TextView = view.find(TopicsItemUI.ID_ReplySize)
        val ivAvatar: ImageView = view.find(TopicsItemUI.ID_Avatar)
    }
}