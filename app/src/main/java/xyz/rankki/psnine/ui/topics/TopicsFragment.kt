package xyz.rankki.psnine.ui.topics

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import xyz.rankki.psnine.base.BaseFragment
import xyz.rankki.psnine.base.BaseTopicsModel
import xyz.rankki.psnine.common.config.RefreshColors
import xyz.rankki.psnine.common.listener.RecyclerViewScrollListener
import xyz.rankki.psnine.data.http.HttpManager
import xyz.rankki.psnine.ui.topic.TopicActivity

class TopicsFragment<K> : BaseFragment(), RecyclerViewScrollListener.LoadingListener {

    private lateinit var mAdapter: TopicsAdapter<K>
    private lateinit var clz: Class<*>
    private var page: Int = 1

    companion object {

        const val ID_SwipeRefreshLayout: Int = 1
        const val ID_RecyclerView: Int = 2

        fun <T, K> newInstance(clz: Class<T>, clazz: Class<K>): TopicsFragment<K> {
            val topicsModel: BaseTopicsModel<*> = clz.newInstance() as BaseTopicsModel<*>
            val args = Bundle()
            val fragment: TopicsFragment<K> = TopicsFragment()

            args.putString("clz", clz.name)
            args.putString("path", topicsModel.getPath())
            args.putString("name", topicsModel.getName())
            fragment.arguments = args

            return fragment
        }
    }

    override fun initView(): View {
        mAdapter = TopicsAdapter(mContext)
        mAdapter.clickListener = View.OnClickListener {
            val position: Int = find<RecyclerView>(ID_RecyclerView).getChildAdapterPosition(it)
            if (position != RecyclerView.NO_POSITION) {
                val extras = Bundle()
                extras.putString("url", mAdapter.getData(position).getTopicUrl())
                ActivityUtils.startActivity(extras, TopicActivity::class.java)
            }
        }
        clz = Class.forName(arguments?.getString("clz")) as Class<*>

        return UI {
            swipeRefreshLayout {
                id = ID_SwipeRefreshLayout
                setColorSchemeColors(RefreshColors.ColorA, RefreshColors.ColorB, RefreshColors.ColorC)
                onRefresh {
                    initData()
                }
                recyclerView {
                    id = ID_RecyclerView
                    layoutManager = LinearLayoutManager(mContext)
                    adapter = mAdapter
                    addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
                    val scrollListener = RecyclerViewScrollListener(mContext, this@TopicsFragment)
                    addOnScrollListener(scrollListener)
                }
            }
        }.view
    }

    override fun initData() {
        setRefreshing(true)
        HttpManager.get()
                .getTopics("${arguments!!.getString("path")}?page=$page", clz)
                .subscribe {
                    mAdapter.updateData(it)
                    setRefreshing(false)
                }
    }

    private fun setRefreshing(isRefreshing: Boolean) {
        find<SwipeRefreshLayout>(ID_SwipeRefreshLayout).isRefreshing = isRefreshing
    }

    override fun loadMore() {
        page += 1
        initData()
    }

    override fun isLoading(): Boolean = find<SwipeRefreshLayout>(ID_SwipeRefreshLayout).isRefreshing

}