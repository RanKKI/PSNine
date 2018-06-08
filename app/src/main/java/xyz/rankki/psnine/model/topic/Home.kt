package xyz.rankki.psnine.model.topic

import me.ghui.fruit.Attrs
import me.ghui.fruit.annotations.Pick
import xyz.rankki.psnine.base.BaseTopicModel
import xyz.rankki.psnine.common.config.PSNineTypes

class Home : BaseTopicModel() {

    @Pick(value = "div.side > div.box > p:nth-child(1) > a > img", attr = Attrs.SRC)
    private var _avatar: String = ""

    @Pick(value = "div.side > div.box > p:nth-child(2) > a")
    private var _username: String = ""

    @Pick(value = "div.main > div:nth-child(1) > div:nth-child(1) > div > span:nth-child(3)")
    private var _meta: String = ""

    @Pick(value = "div.main > div:nth-child(1) > div:nth-child(1) > h1")
    private var _title: String = ""

    @Pick(value = "div.main > div:nth-child(1) > div.content.pd10", attr = Attrs.INNER_HTML)
    private var _content: String = ""

    @Pick(value = "div.main > div.box.mt20 > div.post:has(div.ml64)")
    private var _replies: ArrayList<Reply> = ArrayList()

    @Pick(value = "div.main > div.box.mt20 > div.post > a.btn")
    private var _isMoreReplies: String = ""

    @Pick(value = "div.main > div.box > table.list > tbody > tr")
    var games: ArrayList<Game> = ArrayList()

    @Pick(value = "div.main > div:nth-child(1) > div.content.pd10:has(table)", attr = "itemprop")
    private var _hasTable: String = ""

    @Pick(value = "div.main > div:nth-child(1) > div.content.pd10:has(div.pd10)", attr = "itemprop")
    private var _hasTrophy: String = ""

    override fun getType(): Int = PSNineTypes.Home

    override fun getUsername(): String = _username

    override fun getContent(): String = "<h3>$_title</h3><br>$_content"

    override fun getTime(): String = _meta

    override fun getAvatar(): String = _avatar

    override fun isMoreReplies(): Boolean = _isMoreReplies !== ""

    override fun getReplies(): ArrayList<Reply> = _replies

    override fun usingWebView(): Boolean = _hasTable !== "" || _hasTrophy !== ""

    class Game {
        @Pick(value = "td.pdd10 > a > img", attr = Attrs.SRC)
        var gameAvatar: String = ""

        @Pick(value = "td.pd10 > p > a")
        var gameName: String = ""

        @Pick(value = "td.pd10 > p > span")
        var gameEdition: String = ""

        @Pick(value = "td.pd10 > div.meta")
        var gameTrophy: String = ""

        @Pick(value = "td.pd10 > blockquote")
        var gameComment: String = ""

    }
}
