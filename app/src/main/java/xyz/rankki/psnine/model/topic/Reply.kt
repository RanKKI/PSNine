package xyz.rankki.psnine.model.topic

import me.ghui.fruit.Attrs
import me.ghui.fruit.annotations.Pick


class Reply {
    @Pick(value = "a.l > img", attr = Attrs.SRC)
    val avatar: String = ""
    @Pick(value = "div > div.meta > a.psnnode")
    val username: String = ""
    @Pick(value = "div.post > div > div.meta", attr = Attrs.OWN_TEXT)
    val time: String = ""
    @Pick(value = "div > div.content.pb10", attr = Attrs.INNER_HTML)
    val content: String = ""

    override fun toString(): String {
        return "Reply(avatar='$avatar', username='$username', time='$time', content='$content')"
    }


}