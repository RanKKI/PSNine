package xyz.rankki.psnine.ui.topics

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import xyz.rankki.psnine.base.BaseFragment
import xyz.rankki.psnine.base.BaseTopicsModel
import xyz.rankki.psnine.common.config.RefreshColors
import xyz.rankki.psnine.common.listener.RecyclerViewStopGlideWhenScrollListener
import xyz.rankki.psnine.data.http.HttpManager

class TopicsFragment<K> : BaseFragment() {

    private lateinit var mAdapter: TopicsAdapter<K>
    private lateinit var clz: Class<*>

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
                    addOnScrollListener(RecyclerViewStopGlideWhenScrollListener(mContext))
                }
            }
        }.view
    }

    override fun initData() {
        setRefreshing(true)
        HttpManager.get()
                .getTopics(arguments!!.getString("path"), clz)
                .subscribe {
                    mAdapter.updateData(it)
                    setRefreshing(false)
                }
    }

    private fun setRefreshing(isRefreshing: Boolean) {
        find<SwipeRefreshLayout>(ID_SwipeRefreshLayout).isRefreshing = isRefreshing
    }

}