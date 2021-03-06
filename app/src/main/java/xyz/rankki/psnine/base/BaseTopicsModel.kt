package xyz.rankki.psnine.base

abstract class BaseTopicsModel<T> {

    abstract fun getPath(): String
    abstract fun getName(): String
    abstract fun getType(): Int

    abstract fun getItems(): ArrayList<T>

    abstract class BaseItem {

        abstract fun getTitle(): String
        abstract fun getAvatar(): String
        abstract fun getUsername(): String
        abstract fun getReplySize(): String
        abstract fun getTime(): String
        abstract fun getTopicUrl(): String


    }
}