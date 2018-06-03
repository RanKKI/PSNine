package xyz.rankki.psnine.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {


    lateinit var mContext: FragmentActivity
    private var mRootView: View? = null
    private var isPrepared: Boolean = false
    private var isLoadData: Boolean = false
    private var visibility: Boolean = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.visibility = this.userVisibleHint
        if (this.visibility) {
            lazyLoad()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mContext = activity!!
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (mRootView == null) {
            mRootView = initView()
        }
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.isPrepared = true
        lazyLoad()
    }

    private fun lazyLoad() {

        if (!isPrepared || isLoadData) {
            return
        }

        if (!visibility) {
            return
        }

        initData()
        this.isLoadData = true

    }

    abstract fun initView(): View

    abstract fun initData()
}