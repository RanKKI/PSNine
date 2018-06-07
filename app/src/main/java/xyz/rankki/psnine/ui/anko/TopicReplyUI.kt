package xyz.rankki.psnine.ui.anko

import android.view.View
import android.widget.RelativeLayout
import org.jetbrains.anko.*
import xyz.rankki.psnine.R
import xyz.rankki.psnine.utils.RelativeLayoutParams

class TopicReplyUI<T> : AnkoComponent<T> {

    companion object {
        const val ID_Avatar: Int = 1
        const val ID_Content: Int = 2
        const val ID_Username: Int = 3
        const val ID_Time: Int = 4
    }

    override fun createView(ui: AnkoContext<T>): View = with(ui) {
        relativeLayout {
            padding = 20

            imageView {
                id = ID_Avatar
                imageResource = R.mipmap.ic_launcher

                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayoutParams.ID_MarginRight] = dip(5)
                layoutParams = RelativeLayoutParams().build(dip(40), dip(40), rules)
            }

            textView {
                id = ID_Content
                topPadding = 10

                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.RIGHT_OF] = ID_Avatar
                layoutParams = RelativeLayoutParams().build(matchParent, wrapContent, rules)

            }

            textView {
                id = ID_Username
                text = "placeholder"

                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.RIGHT_OF] = ID_Avatar
                rules[RelativeLayout.BELOW] = ID_Content
                rules[RelativeLayoutParams.ID_MarginRight] = dip(5)
                layoutParams = RelativeLayoutParams().build(wrapContent, wrapContent, rules)
            }

            textView {
                id = ID_Time
                textSize = 10f
                text = "10s ago"

                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.RIGHT_OF] = ID_Username
                rules[RelativeLayout.ALIGN_BASELINE] = ID_Username
                rules[RelativeLayoutParams.ID_MarginRight] = dip(5)
                layoutParams = RelativeLayoutParams().build(wrapContent, wrapContent, rules)
            }

        }
    }
}