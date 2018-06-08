package xyz.rankki.psnine.model.topics

import xyz.rankki.psnine.common.config.PSNineTypes

class Guide : Home() {

    override fun getPath(): String = "node/guide"

    override fun getName(): String = "攻略"

    override fun getType(): Int = PSNineTypes.Guide

    class Topic : Home.Topic()
}