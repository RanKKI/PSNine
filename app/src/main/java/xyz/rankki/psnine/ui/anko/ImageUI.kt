package xyz.rankki.psnine.ui.anko

import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*

class ImageUI<T>(private val paddingSides: Int = 50) : AnkoComponent<T> {
    companion object {
        const val ID_Image = 1
    }

    override fun createView(ui: AnkoContext<T>): View = with(ui) {
        imageView {
            id = ID_Image

            rightPadding = paddingSides
            leftPadding = paddingSides
            topPadding = 10
            bottomPadding = 10
            layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
        }
    }

}