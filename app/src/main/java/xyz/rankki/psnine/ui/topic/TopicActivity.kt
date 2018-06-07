package xyz.rankki.psnine.ui.topic

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import android.widget.Toolbar
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.find
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.toolbar
import org.jetbrains.anko.verticalLayout
import xyz.rankki.psnine.R
import xyz.rankki.psnine.common.config.RefreshColors
import xyz.rankki.psnine.common.listener.RecyclerViewStopGlideWhenScrollListener
import xyz.rankki.psnine.data.http.HttpManager
import xyz.rankki.psnine.model.topic.Gene
import xyz.rankki.psnine.model.topic.Home
import xyz.rankki.psnine.ui.topics.TopicsFragment
import xyz.rankki.psnine.utils.ExtraSpaceLinearLayoutManager

class TopicActivity : AppCompatActivity() {

    companion object {
        const val ID_SwipeRefreshLayout: Int = 1
        const val ID_RecyclerView: Int = 2
        const val ID_Toolbar = 3
    }

    private val mContext: Context = this
    private lateinit var mAdapter: TopicAdapter
    private lateinit var clz: Class<*>
    private lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = TopicAdapter(mContext)
        verticalLayout {
            toolbar {
                id = ID_Toolbar
                backgroundColorResource = R.color.colorAppBarBackground
                setTitleTextColor(resources.getColor(R.color.colorAppBarText))
            }
            swipeRefreshLayout {
                id = ID_SwipeRefreshLayout
                setColorSchemeColors(RefreshColors.ColorA, RefreshColors.ColorB, RefreshColors.ColorC)
                onRefresh {
                    initData()
                }
                recyclerView {
                    id = ID_RecyclerView
                    val lm = ExtraSpaceLinearLayoutManager(mContext)
                    lm.addExtraSpace(ScreenUtils.getScreenHeight())
                    layoutManager = lm
                    adapter = mAdapter
                    setItemViewCacheSize(60)
                    isDrawingCacheEnabled = true
                    drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
                    addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
                    addOnScrollListener(RecyclerViewStopGlideWhenScrollListener(mContext))
                }
            }
        }

        path = intent.extras.getString("url")
                .replace("http://psnine.com/", "")
        find<Toolbar>(ID_Toolbar).title = path.split("/")[1]
        clz = when (path.split("/")[0]) { //topic type
            "gene" -> Gene::class.java
            "topic" -> Home::class.java
            else -> {
                ToastUtils.showShort("Invalid Url")
                finish()
                return
            }
        }

        initData()
    }

    private fun initData() {
        setRefreshing(true)
        HttpManager.get()
                .getTopic(path, clz)
                .subscribe {
                    mAdapter.update(it)
                    setRefreshing(false)
                }
    }

    private fun setRefreshing(isRefreshing: Boolean) {
        find<SwipeRefreshLayout>(TopicsFragment.ID_SwipeRefreshLayout).isRefreshing = isRefreshing
    }

}
