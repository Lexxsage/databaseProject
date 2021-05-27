package com.lexxsage.nanopost.db

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

interface Timestamped {
    val dateCreated: Long
}

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(Users)

    var username by Users.username
    var passwordHash by Users.passwordHash
    var passwordUpdated by Users.passwordUpdated
    var pushToken by Users.pushToken

    var displayName by Users.displayName
    var bio by Users.bio
    var avatar by ImageEntity optionalReferencedOn Users.avatar

    val images by ImageEntity referrersOn Images.owner
    val posts by PostEntity referrersOn Posts.owner
    val subscribers by UserEntity.via(Subscriptions.user, Subscriptions.subscriber)
    var subscriptions by UserEntity.via(Subscriptions.subscriber, Subscriptions.user)
    val likedPosts by PostEntity via PostsLikes

    override fun equals(other: Any?) = other is UserEntity && id == other.id
    override fun hashCode() = id.hashCode()
}

class ImageEntity(id: EntityID<UUID>) : UUIDEntity(id), Timestamped {
    companion object : UUIDEntityClass<ImageEntity>(Images)

    var owner by UserEntity referencedOn Images.owner
    override var dateCreated by Images.dateCreated
    var width by Images.width
    var height by Images.height
    var url by Images.url

    override fun equals(other: Any?) = other is ImageEntity && id == other.id
    override fun hashCode() = id.hashCode()
}

class PostEntity(id: EntityID<UUID>) : UUIDEntity(id), Timestamped {
    companion object : UUIDEntityClass<PostEntity>(Posts)

    var owner by UserEntity referencedOn Posts.owner
    override var dateCreated by Posts.dateCreated
    var text by Posts.text
    var image by ImageEntity optionalReferencedOn Posts.image
    var likes by UserEntity via PostsLikes

    override fun equals(other: Any?) = other is PostEntity && id == other.id
    override fun hashCode() = id.hashCode()
}
