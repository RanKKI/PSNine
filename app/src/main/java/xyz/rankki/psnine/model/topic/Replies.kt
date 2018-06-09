package xyz.rankki.psnine.model.topic

import me.ghui.fruit.annotations.Pick

class Replies {

    @Pick(value = "body > div.min-inner.mt40 > div.box.mt20 > div.post:has(a.l)")
    var replies: ArrayList<Reply> = ArrayList()

    @Pick(value = "div.page:nth-child(1) > ul > li:not(.disabled) > a")
    private var maxPage: ArrayList<String> = ArrayList(1)

    fun getMaxPage(): Int = maxPage[maxPage.size - 1].toInt()

}