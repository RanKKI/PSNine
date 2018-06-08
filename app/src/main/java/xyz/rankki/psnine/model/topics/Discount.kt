package xyz.rankki.psnine.model.topics

import xyz.rankki.psnine.common.config.PSNineTypes


class Discount : Home() {

    override fun getPath(): String = "node/off"

    override fun getName(): String = "折扣"

    override fun getType(): Int = PSNineTypes.Discount

    class Topic : Home.Topic()
}