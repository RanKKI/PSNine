package xyz.rankki.psnine.model.topics

import xyz.rankki.psnine.common.config.PSNineTypes

class OpenBox : Home() {

    override fun getPath(): String = "node/openbox"

    override fun getName(): String = "开箱"

    override fun getType(): Int = PSNineTypes.OpenBox

    class Topic : Home.Topic()
}