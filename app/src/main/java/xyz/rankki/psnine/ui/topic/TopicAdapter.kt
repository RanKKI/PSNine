package xyz.rankki.psnine.ui.topic

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import org.jetbrains.anko.*
import xyz.rankki.psnine.R
import xyz.rankki.psnine.base.BaseTopicModel
import xyz.rankki.psnine.common.config.PSNineTypes
import xyz.rankki.psnine.model.topic.Gene
import xyz.rankki.psnine.model.topic.Home
import xyz.rankki.psnine.model.topic.Reply
import xyz.rankki.psnine.ui.anko.*
import xyz.rankki.psnine.utils.HtmlUtil
import java.io.BufferedReader
import java.io.InputStreamReader

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
    private var _replyPositionOffset = 0
    private var _imagePositionOffset = 0
    private var _gamesPositionOffset = 0

    private var _images: ArrayList<Gene.Image> = ArrayList()
    private var _games: ArrayList<Home.Game> = ArrayList()

    fun updateTopic(topic: BaseTopicModel) {
        doAsync {
            _topic = topic
            _replyPositionOffset = -1
            when (_topic?.getType()) {
                PSNineTypes.Gene -> {
                    val gene = _topic!! as Gene
                    _replyPositionOffset -= gene.images.size
                    _images = gene.images
                    _imagePositionOffset = -1
                }
                PSNineTypes.Home -> {
                    val home = _topic!! as Home
                    _replyPositionOffset -= home.games.size
                    _games = home.games
                    _gamesPositionOffset = -1
                    _gamesPositionOffset -= _imagePositionOffset
                }
            }
            uiThread {
                notifyItemRangeChanged(0, itemCount)
            }
        }
    }

    fun updateReplies(replies: ArrayList<Reply>) {
        doAsync {
            val start: Int = itemCount
            _replies.addAll(replies)
            uiThread {
                notifyItemRangeInserted(start, _replies.size)
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

    override fun getItemCount(): Int {
        return doAsyncResult {
            if (_topic === null) {
                return@doAsyncResult 0
            }
            var count = 1 // Header view
            count += _images.size // add Gene Images Views
            count += _games.size
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
                if (_topic!!.getType() == PSNineTypes.Gene || !(_topic!!.usingWebView())) {
                    viewHolder.wvContent.visibility = View.GONE
                    viewHolder.tvContent.visibility = View.VISIBLE
                    HtmlUtil.fromHtml(mContext, _topic!!.getContent(), viewHolder.tvContent)
                } else {
                    viewHolder.wvContent.visibility = View.VISIBLE
                    viewHolder.tvContent.visibility = View.GONE
                    doAsync {
                        val inputStream = mContext.resources.openRawResource(R.raw.p9css)
                        val css = BufferedReader(InputStreamReader(inputStream))
                                .readLines().joinToString("\n")
                        val content = "<html><head><style>$css</style></head><body>${_topic!!.getContent()}</body></html>"
                        uiThread {
                            viewHolder.wvContent.loadData(content, "text/html", "utf-8")
                        }
                    }
                }
            }

            Type_Image -> {
                val imagePosition: Int = _imagePositionOffset + position
                val viewHolder: ImageViewHolder = holder as ImageViewHolder
                val requestOptions = RequestOptions()
                requestOptions.override(Target.SIZE_ORIGINAL)
                Glide.with(mContext)
                        .setDefaultRequestOptions(requestOptions)
                        .load((_topic!! as Gene).images[imagePosition].url)
                        .into(viewHolder.ivImage)

            }

            Type_Game -> {
                val viewHolder: GameViewHolder = holder as GameViewHolder
                val game: Home.Game = _games[_gamesPositionOffset + position]
                viewHolder.tvGameEdition.text = game.gameEdition
                viewHolder.tvGameName.text = game.gameName
                viewHolder.tvGameTrophy.text = game.gameTrophy
                Glide.with(mContext).load(game.gameAvatar).into(viewHolder.ivGameAvatar)
                if (game.gameComment !== "") {
                    viewHolder.tvComment.visibility = View.VISIBLE
                    viewHolder.tvComment.text = game.gameComment
                }

            }

            Type_MoreReplies -> {

            }

            Type_Reply -> {
                val viewHolder: ReplyViewHolder = holder as ReplyViewHolder
                val reply: Reply = _replies[_replyPositionOffset + position]
                viewHolder.tvUsername.text = reply.username
                Glide.with(mContext).load(reply.avatar).into(viewHolder.ivAvatar)
                HtmlUtil.fromHtml(mContext, reply.content, viewHolder.tvContent)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return doAsyncResult {
            if (position == 0) {
                return@doAsyncResult Type_Header
            } else if (position <= _images.size) {
                return@doAsyncResult Type_Image
            } else if (_images.size < position && position <= _games.size + _images.size) {
                return@doAsyncResult Type_Game
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
        val wvContent: WebView = mView.find(TopicHeaderUI.ID_Content_WebView)
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

    class GameViewHolder(mView: View) : ViewHolder(mView) {
        val tvGameName: TextView = mView.find(GameUI.ID_GameName)
        val tvGameTrophy: TextView = mView.find(GameUI.ID_Trophy)
        val tvGameEdition: TextView = mView.find(GameUI.ID_Edition)
        val tvComment: TextView = mView.find(GameUI.ID_Comment)
        val ivGameAvatar: ImageView = mView.find(GameUI.ID_GameAvatar)

    }


}