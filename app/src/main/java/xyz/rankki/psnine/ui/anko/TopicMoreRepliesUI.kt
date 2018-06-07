package xyz.rankki.psnine.ui.anko

import android.view.View
import org.jetbrains.anko.*
import xyz.rankki.psnine.utils.RelativeLayoutParams

class TopicMoreRepliesUI<T> : AnkoComponent<T> {
    override fun createView(ui: AnkoContext<T>): View = with(ui) {
        relativeLayout {
            layoutParams = RelativeLayoutParams().build(matchParent, wrapContent, null)
            padding = 20
            button {
                text = "查看更多回复"
            }.lparams(matchParent, wrapContent)
        }
    }

}