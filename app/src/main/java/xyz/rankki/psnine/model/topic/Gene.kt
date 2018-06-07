package xyz.rankki.psnine.model.topic

import me.ghui.fruit.Attrs
import me.ghui.fruit.annotations.Pick
import xyz.rankki.psnine.base.BaseTopicModel
import xyz.rankki.psnine.common.config.PSNineTypes

class Gene : BaseTopicModel() {

    @Pick(value = "div.side > div > p:nth-child(1) > a > img", attr = Attrs.SRC)
    private var _avatar: String = ""

    @Pick(value = "div.side > div > p:nth-child(2) > a")
    private var _username: String = ""

    @Pick(value = "div.main > div:nth-child(1) > div:nth-child(1) > div.meta")
    private var _meta: String = ""

    @Pick(value = "div.main > div:nth-child(1) > div:nth-child(1) > div.content.pb10", attr = Attrs.INNER_HTML)
    private var _content: String = ""

    @Pick(value = "div.main > div.box.mt20 > div.post:has(div.ml64)")
    private var _replies: ArrayList<Reply> = ArrayList()

    @Pick(value = "div.main > div.box.mt20 > div.post > a.btn")
    private var _isMoreReplies: String = ""

    @Pick(value = "div.main > div:nth-child(1) > div.content.pd10 > p")
    var images: ArrayList<Image> = ArrayList()

    override fun getType(): Int = PSNineTypes.Gene

    override fun getUsername(): String = _username

    override fun getContent(): String = _content

    override fun getTime(): String = _meta.split(" ")[0]

    override fun getAvatar(): String = _avatar

    override fun isMoreReplies(): Boolean = _isMoreReplies !== ""

    override fun getReplies(): ArrayList<Reply> = _replies

    class Image {

        @Pick(value = "a > img", attr = Attrs.SRC)
        var url: String = ""

    }
}