package xyz.rankki.psnine.ui.anko

import android.view.View
import android.widget.RelativeLayout
import org.jetbrains.anko.*
import xyz.rankki.psnine.R
import xyz.rankki.psnine.utils.RelativeLayoutParams

class TopicsItemUI<T> : AnkoComponent<T> {

    companion object {
        const val ID_Avatar: Int = 1
        const val ID_Title: Int = 2
        const val ID_Username: Int = 3
        const val ID_Time: Int = 4
        const val ID_ReplySize: Int = 5

    }

    override fun createView(ui: AnkoContext<T>): View = with(ui) {
        relativeLayout {
            padding = 10

            imageView {
                id = ID_Avatar
                imageResource = R.mipmap.ic_launcher

                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayoutParams.ID_MarginRight] = dip(5)
                layoutParams = RelativeLayoutParams().build(dip(45), dip(45), rules)
            }

            textView {
                id = ID_Title
                singleLine = true
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
                rules[RelativeLayout.BELOW] = ID_Title
                rules[RelativeLayoutParams.ID_MarginRight] = dip(5)
                layoutParams = RelativeLayoutParams().build(wrapContent, wrapContent, rules)
            }

            textView {
                id = ID_ReplySize
                textSize = 10f
                text = "10s ago"

                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.RIGHT_OF] = ID_Username
                rules[RelativeLayout.ALIGN_BASELINE] = ID_Username
                rules[RelativeLayoutParams.ID_MarginRight] = dip(5)
                layoutParams = RelativeLayoutParams().build(wrapContent, wrapContent, rules)
            }

            textView {
                id = ID_Time
                textSize = 10f
                text = "123"

                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.RIGHT_OF] = ID_ReplySize
                rules[RelativeLayout.ALIGN_BASELINE] = ID_ReplySize
                layoutParams = RelativeLayoutParams().build(wrapContent, wrapContent, rules)
            }

        }
    }

}