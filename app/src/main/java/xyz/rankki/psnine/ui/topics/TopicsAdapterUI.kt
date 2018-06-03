package xyz.rankki.psnine.ui.topics

import android.view.View
import android.widget.RelativeLayout
import org.jetbrains.anko.*
import xyz.rankki.psnine.R


class TopicsAdapterUI<T> : AnkoComponent<TopicsAdapter<T>> {

    companion object {
        const val ID_Avatar: Int = 1
        const val ID_Title: Int = 2
        const val ID_Username: Int = 3
        const val ID_Info: Int = 4


        const val ID_MarginBottom = 10
        const val ID_MarginTop = 11
        const val ID_MarginRight = 12
        const val ID_MarginLeft = 13

    }

    private fun buildLayoutParams(w: Int, h: Int, args: Map<Int, Int>?): RelativeLayout.LayoutParams {
        val lp = RelativeLayout.LayoutParams(w, h)
        args?.forEach({
            when (it.key) {
                ID_MarginBottom -> lp.bottomMargin = it.value
                ID_MarginLeft -> lp.leftMargin = it.value
                ID_MarginRight -> lp.rightMargin = it.value
                ID_MarginTop -> lp.topMargin = it.value
                else -> lp.addRule(it.key, it.value)
            }
        })
        return lp
    }

    override fun createView(ui: AnkoContext<TopicsAdapter<T>>): View = with(ui) {
        relativeLayout {
            padding = 10

            imageView {
                id = ID_Avatar
                imageResource = R.mipmap.ic_launcher

                val rules: MutableMap<Int, Int> = HashMap()
                rules[ID_MarginRight] = dip(5)
                layoutParams = buildLayoutParams(dip(50), dip(50), rules)
            }

            textView {
                id = ID_Title
                singleLine = true

                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.RIGHT_OF] = ID_Avatar
                layoutParams = buildLayoutParams(matchParent, wrapContent, rules)

                topPadding = 10
            }

            textView {
                id = ID_Username

                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.RIGHT_OF] = ID_Avatar
                rules[RelativeLayout.BELOW] = ID_Title
                rules[ID_MarginRight] = dip(5)
                layoutParams = buildLayoutParams(wrapContent, wrapContent, rules)
            }

            textView {
                id = ID_Info
                textSize = 10f

                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.RIGHT_OF] = ID_Username
                rules[RelativeLayout.BELOW] = ID_Title
                rules[RelativeLayout.ALIGN_BASELINE] = ID_Username
                layoutParams = buildLayoutParams(wrapContent, wrapContent, rules)
            }

        }
    }
}