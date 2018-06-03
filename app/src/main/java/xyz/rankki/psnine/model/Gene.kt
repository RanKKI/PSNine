package xyz.rankki.psnine.model

import me.ghui.fruit.Attrs
import me.ghui.fruit.annotations.Pick
import xyz.rankki.psnine.base.BaseModel


class Gene : BaseModel<Gene.Topic>() {

    @Pick(value = "ul.list.genelist > li:has(a)")
    private var _topics: ArrayList<Topic> = ArrayList()

    override fun getItems(): ArrayList<Topic> = _topics

    override fun getPath(): String = "gene"
    override fun getName(): String = "基因"

    class Topic : BaseModel.BaseItem() {

        @Pick(value = "a.l > img", attr = Attrs.SRC)
        private var _avatar: String = ""

        @Pick(value = "div.ml64 > div.meta > a.psnnode")
        private var _username: String = ""

        @Pick(value = "div.ml64 > div.meta", attr = Attrs.OWN_TEXT)
        private var _info: String = ""

        @Pick(value = "div.ml64 > a > div.content")
        private var _content: String = ""

        @Pick(value = "div.ml64 > a.touch", attr = Attrs.HREF)
        private var _url: String = ""

        override fun getTitle(): String = _content

        override fun getAvatar(): String = _avatar

        override fun getUsername(): String = _username

        override fun getReplySize(): String = _info.split(" ")[0]

        override fun getTime(): String = _info.split(" ")[1]

        override fun getTopicUrl(): String = _url
    }

}