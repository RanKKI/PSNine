package xyz.rankki.psnine.ui.topics

import android.content.Context
import xyz.rankki.psnine.base.BasePresenter
import xyz.rankki.psnine.base.BaseView

interface TopicsContract {

    interface View : BaseView<Presenter> {

        fun setRefreshing(isRefreshing: Boolean)

        fun getContext(): Context

        fun updateData(list: ArrayList<*>)

        fun getPath(): String
    }

    interface Presenter : BasePresenter {

        fun loadData()
    }
}