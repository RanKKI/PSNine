package xyz.rankki.psnine.ui.topic

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.jetbrains.anko.*
import xyz.rankki.psnine.base.BaseTopicModel
import xyz.rankki.psnine.common.config.PSNineTypes
import xyz.rankki.psnine.model.topic.Gene
import xyz.rankki.psnine.model.topic.Home
import xyz.rankki.psnine.model.topic.Reply
import xyz.rankki.psnine.ui.anko.*
import xyz.rankki.psnine.utils.HtmlUtil

class TopicAdapter(private val mContext: Context) : RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

    companion object {
        const val Type_Header = 10
        const val Type_MoreReplies = 11
        const val Type_Reply = 12
        const val Type_Image = 13
        const val Type_Game = 14
    }

    private val _ankoContext = AnkoContext.createReusable(mContext, this)
    private var _topic: BaseTopicModel? = null
    private var _replies: ArrayList<Reply> = ArrayList()
    private var _replyPositionOffset = -1
    private var _imagePositionOffset = -1

    private var _images: ArrayList<Gene.Image> = ArrayList()
    private var _games: ArrayList<Home.Game> = ArrayList()

    fun update(topic: BaseTopicModel) {
        doAsync {
            _replyPositionOffset = -1
            _imagePositionOffset = -1
            _replies = topic.getReplies()
            _topic = topic
            if (_topic!!.isMoreReplies()) {
                _replyPositionOffset -= 1
            }
            if (_topic?.getType() == PSNineTypes.Gene) {
                val gene = _topic!! as Gene
                _replyPositionOffset -= gene.images.size
                _images = gene.images
            }

            if (_topic?.getType() == PSNineTypes.Home) {
                val home = _topic!! as Home
                _replyPositionOffset -= home.games.size
                _games = home.games
            }
            uiThread {
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            Type_Header -> HeaderViewHolder(TopicHeaderUI<TopicAdapter>().createView(_ankoContext))
            Type_MoreReplies -> MoreRepliesViewHolder(TopicMoreRepliesUI<TopicAdapter>().createView(_ankoContext))
            Type_Image -> ImageViewHolder(ImageUI<TopicAdapter>().createView(_ankoContext))
            Type_Game -> GameViewHolder(GameUI<TopicAdapter>().createView(_ankoContext))
            else -> ReplyViewHolder(TopicReplyUI<TopicAdapter>().createView(_ankoContext))
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return doAsyncResult {
            if (_topic === null) {
                return@doAsyncResult 0
            }
            var count = 1 // Header view
            count += _images.size // add Gene Images Views
            count += _games.size
            if (_topic!!.isMoreReplies()) {
                count += 1 // view more replies button
            }
            count += _replies.size // add replies's size
            return@doAsyncResult count
        }.get()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            Type_Header -> {
                val viewHolder: HeaderViewHolder = holder as HeaderViewHolder
                viewHolder.tvUsername.text = _topic!!.getUsername()
                Glide.with(mContext).load(_topic!!.getAvatar()).into(viewHolder.ivAvatar)
                HtmlUtil.setHtml(mContext, viewHolder.tvContent, _topic!!.getContent())
            }

            Type_Image -> {
                val imagePosition: Int = _imagePositionOffset + position
                val viewHolder: ImageViewHolder = holder as ImageViewHolder
                Glide.with(mContext)
                        .load((_topic!! as Gene).images[imagePosition].url)
                        .into(viewHolder.ivImage)

            }

            Type_Game -> {

            }

            Type_MoreReplies -> {

            }

            Type_Reply -> {
                val viewHolder: ReplyViewHolder = holder as ReplyViewHolder
                val reply: Reply = _replies[_replyPositionOffset + position]
                viewHolder.tvUsername.text = reply.username
                Glide.with(mContext).load(reply.avatar).into(viewHolder.ivAvatar)
                HtmlUtil.setHtml(mContext, viewHolder.tvContent, reply.content)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return doAsyncResult {
            if (position == 0) {
                return@doAsyncResult Type_Header
            } else if (0 < position && position < _images.size + 1) {
                return@doAsyncResult Type_Image
            } else if (0 + _images.size < position && position < _games.size + _images.size + 1) {
                return@doAsyncResult Type_Game
            } else if (_topic!!.isMoreReplies() && position == _games.size + _images.size + 1) {
                return@doAsyncResult Type_MoreReplies
            } else {
                return@doAsyncResult Type_Reply
            }
        }.get()
    }

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class HeaderViewHolder(mView: View) : ViewHolder(mView) {
        val tvUsername: TextView = mView.find(TopicHeaderUI.ID_Username)
        val tvContent: TextView = mView.find(TopicHeaderUI.ID_Content)
        val ivAvatar: ImageView = mView.find(TopicHeaderUI.ID_Avatar)
    }

    class ReplyViewHolder(mView: View) : ViewHolder(mView) {
        val tvUsername: TextView = mView.find(TopicReplyUI.ID_Username)
        val tvContent: TextView = mView.find(TopicReplyUI.ID_Content)
        val ivAvatar: ImageView = mView.find(TopicReplyUI.ID_Avatar)
    }

    class MoreRepliesViewHolder(mView: View) : ViewHolder(mView)

    class ImageViewHolder(mView: View) : ViewHolder(mView) {
        val ivImage: ImageView = mView.find(ImageUI.ID_Image)
    }

    class GameViewHolder(mView: View) : ViewHolder(mView)


}