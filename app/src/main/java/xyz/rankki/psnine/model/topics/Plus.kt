package xyz.rankki.psnine.model.topics

import xyz.rankki.psnine.common.config.PSNineTypes

class Plus : Home() {

    override fun getPath(): String = "node/plus"

    override fun getName(): String = "会面"

    override fun getType(): Int = PSNineTypes.Plus

    class Topic : Home.Topic()
}