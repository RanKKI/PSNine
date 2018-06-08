package xyz.rankki.psnine.utils

import android.widget.RelativeLayout
import org.jetbrains.anko.margin

class RelativeLayoutParams {

    companion object {
        const val ID_MarginBottom = 10
        const val ID_MarginTop = 11
        const val ID_MarginRight = 12
        const val ID_MarginLeft = 13
        const val ID_Margin = 14
    }

    fun build(w: Int, h: Int, args: Map<Int, Int>?): RelativeLayout.LayoutParams {
        val lp = RelativeLayout.LayoutParams(w, h)
        args?.forEach({
            when (it.key) {
                ID_MarginBottom -> lp.bottomMargin = it.value
                ID_MarginLeft -> lp.leftMargin = it.value
                ID_MarginRight -> lp.rightMargin = it.value
                ID_MarginTop -> lp.topMargin = it.value
                ID_Margin -> lp.margin = it.value
                else -> lp.addRule(it.key, it.value)
            }
        })
        return lp
    }

}