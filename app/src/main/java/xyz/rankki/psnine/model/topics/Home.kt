package xyz.rankki.psnine.model.topics

import me.ghui.fruit.Attrs
import me.ghui.fruit.annotations.Pick
import xyz.rankki.psnine.base.BaseTopicsModel
import xyz.rankki.psnine.common.config.PSNineTypes


open class Home : BaseTopicsModel<Home.Topic>() {

    @Pick(value = "ul.list > li")
    private var _topics: ArrayList<Topic> = ArrayList()

    override fun getItems(): ArrayList<Topic> = _topics

    override fun getPath(): String = "topic"
    override fun getName(): String = "主页"
    override fun getType(): Int = PSNineTypes.Home


    open class Topic : BaseTopicsModel.BaseItem() {

        @Pick(value = "a.l > img", attr = Attrs.SRC)
        private var _avatar: String = ""
        @Pick(value = "a.psnnode")
        private var _username: String = ""
        @Pick(value = "div.ml64 > div.meta", attr = Attrs.OWN_TEXT)
        private var _time: String = ""
        @Pick(value = "a.rep.r")
        private var _reply = "0"
        @Pick(value = "div.ml64 > div.title > a")
        private var _content: String = ""
        @Pick(value = "div.ml64 > div.title > a", attr = Attrs.HREF)
        private var _url: String = ""

        override fun getTitle(): String = _content

        override fun getAvatar(): String = _avatar

        override fun getUsername(): String = _username

        override fun getReplySize(): String = _reply + "评论"

        override fun getTime(): String = _time

        override fun getTopicUrl(): String = _url

    }
}