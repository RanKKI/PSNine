package xyz.rankki.psnine.ui.anko

import android.view.View
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.textView

class GameUI<T> : AnkoComponent<T> {
    override fun createView(ui: AnkoContext<T>): View = with(ui) {
        relativeLayout {
            textView {
                text = "a b test, c d test"
            }
        }
    }

}