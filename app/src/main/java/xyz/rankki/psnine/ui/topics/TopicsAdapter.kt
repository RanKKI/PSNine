package xyz.rankki.psnine.ui.topics

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import xyz.rankki.psnine.base.BaseTopicsModel
import xyz.rankki.psnine.ui.anko.TopicsItemUI
import xyz.rankki.psnine.ui.topic.TopicActivity

@Suppress("UNCHECKED_CAST")
class TopicsAdapter<T>(private val mContext: Context) : RecyclerView.Adapter<TopicsAdapter.ViewHolder>() {

    private val ankoContext = AnkoContext.createReusable(mContext, this)

    private var data: ArrayList<T> = ArrayList()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TopicsItemUI<TopicsAdapter<T>>().createView(ankoContext))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = data[position] as BaseTopicsModel.BaseItem
        holder.view.onClick {
            doAsync {
                val extras = Bundle()
                extras.putString("url", topic.getTopicUrl())
                ActivityUtils.startActivity(extras, TopicActivity::class.java)
            }
        }
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