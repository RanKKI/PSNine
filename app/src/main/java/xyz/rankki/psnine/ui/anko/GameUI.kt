package xyz.rankki.psnine.ui.anko

import android.view.View
import android.widget.RelativeLayout
import org.jetbrains.anko.*
import xyz.rankki.psnine.R
import xyz.rankki.psnine.utils.RelativeLayoutParams

class GameUI<T> : AnkoComponent<T> {

    companion object {
        const val ID_GameAvatar = 1
        const val ID_Edition = 2
        const val ID_GameName = 3
        const val ID_Trophy = 4
        const val ID_Comment = 5
    }

    override fun createView(ui: AnkoContext<T>): View = with(ui) {
        relativeLayout {
            padding = 10
            imageView {
                id = ID_GameAvatar
                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayoutParams.ID_Margin] = 10
                layoutParams = RelativeLayoutParams().build(dip(91), dip(50), null)
            }

            textView {
                id = ID_Edition
                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.RIGHT_OF] = ID_GameAvatar
                rules[RelativeLayout.ALIGN_TOP] = ID_GameAvatar
                rules[RelativeLayoutParams.ID_MarginRight] = 5
                rules[RelativeLayoutParams.ID_MarginLeft] = 5
                layoutParams = RelativeLayoutParams().build(wrapContent, wrapContent, rules)
            }

            textView {
                id = ID_GameName
                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.RIGHT_OF] = ID_Edition
                rules[RelativeLayout.ALIGN_BASELINE] = ID_Edition
                layoutParams = RelativeLayoutParams().build(wrapContent, wrapContent, rules)
            }

            textView {
                id = ID_Trophy
                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.RIGHT_OF] = ID_GameAvatar
                rules[RelativeLayout.ALIGN_BOTTOM] = ID_GameAvatar
                layoutParams = RelativeLayoutParams().build(wrapContent, wrapContent, rules)
            }

            textView {
                id = ID_Comment
                visibility = View.GONE
                padding = 10
                backgroundColor = resources.getColor(R.color.colorBlockQuoteBackground)
                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.RIGHT_OF] = ID_GameAvatar
                rules[RelativeLayout.BELOW] = ID_Trophy
                layoutParams = RelativeLayoutParams().build(matchParent, wrapContent, rules)
            }

        }
    }

}