package xyz.rankki.psnine.ui.topics

import xyz.rankki.psnine.base.BaseModel
import xyz.rankki.psnine.data.http.HttpManager

class TopicsPresenter<T>(private val mView: TopicsContract.View, private val clz: Class<T>) : TopicsContract.Presenter {

    init {
        mView.setPresenter(this)
    }

    override fun start() {
        loadData()
    }

    override fun loadData() {
        mView.setRefreshing(true)
        HttpManager.get()
                .getTopics(mView.getPath(), clz)
                .subscribe {
                    mView.updateData(it)
                    mView.setRefreshing(false)
                }
    }

}