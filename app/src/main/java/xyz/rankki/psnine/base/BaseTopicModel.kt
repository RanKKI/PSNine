package xyz.rankki.psnine.base

import xyz.rankki.psnine.model.topic.Reply

abstract class BaseTopicModel {

    abstract fun getType(): Int

    abstract fun getUsername(): String

    abstract fun getContent(): String

    abstract fun getTime(): String

    abstract fun getAvatar(): String

    abstract fun isMoreReplies(): Boolean

    abstract fun getReplies(): ArrayList<Reply>

}