package xyz.rankki.psnine.ui.anko

import android.view.View
import android.widget.RelativeLayout
import org.jetbrains.anko.*
import xyz.rankki.psnine.R
import xyz.rankki.psnine.utils.RelativeLayoutParams

class TopicHeaderUI<T> : AnkoComponent<T> {

    companion object {

        const val ID_Avatar: Int = 1
        const val ID_Content: Int = 2
        const val ID_Username: Int = 3
        const val ID_Content_WebView = 4

    }

    override fun createView(ui: AnkoContext<T>): View = with(ui) {
        relativeLayout {
            padding = 10

            imageView {
                id = ID_Avatar
                imageResource = R.mipmap.ic_launcher

                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayoutParams.ID_MarginRight] = dip(5)
                layoutParams = RelativeLayoutParams().build(dip(50), dip(50), rules)
            }

            textView {
                id = ID_Username
                topPadding = 10

                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.RIGHT_OF] = ID_Avatar
                layoutParams = RelativeLayoutParams().build(wrapContent, wrapContent, rules)

            }

            textView {
                id = ID_Content
                topPadding = 5

                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.BELOW] = ID_Avatar
                layoutParams = RelativeLayoutParams().build(matchParent, wrapContent, rules)

            }

            webView {
                id = ID_Content_WebView
                topPadding = 5
                visibility = View.GONE
                backgroundColor = 0xFFFFFF
                isHorizontalScrollBarEnabled = false

                val websetting = this.settings
                websetting.loadWithOverviewMode = true
                val rules: MutableMap<Int, Int> = HashMap()
                rules[RelativeLayout.BELOW] = ID_Avatar
                layoutParams = RelativeLayoutParams().build(matchParent, wrapContent, rules)

            }

        }
    }

}