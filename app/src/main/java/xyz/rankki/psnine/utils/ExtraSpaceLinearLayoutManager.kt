package xyz.rankki.psnine.utils

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class ExtraSpaceLinearLayoutManager(mContext: Context) : LinearLayoutManager(mContext) {

    private var extraSpace = -1

    fun addExtraSpace(extraSpace: Int) {
        this.extraSpace = extraSpace
    }

    override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
        return if (extraSpace == -1) {
            super.getExtraLayoutSpace(state)
        } else {
            extraSpace
        }
    }

}