package xyz.rankki.psnine.model

import me.ghui.fruit.Attrs
import me.ghui.fruit.annotations.Pick
import xyz.rankki.psnine.base.BaseModel


class Topics : BaseModel<Topics.Topic>() {

    @Pick(value = "ul.list > li")
    private var _topics: ArrayList<Topic> = ArrayList()

    override fun getItems(): ArrayList<Topic> = _topics

    override fun getPath(): String = ""
    override fun getName(): String = "主页"


    class Topic : BaseModel.BaseItem() {

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

        override fun getReplySize(): String = _reply

        override fun getTime(): String = _time

        override fun getTopicUrl(): String = _url

    }
}