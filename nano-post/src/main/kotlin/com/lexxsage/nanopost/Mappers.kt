package com.lexxsage.nanopost

import com.lexxsage.nanopost.db.ImageEntity
import com.lexxsage.nanopost.db.PostEntity
import com.lexxsage.nanopost.db.UserEntity
import com.lexxsage.nanopost.db.transaction
import java.util.*

suspend fun UserEntity.avatar() = transaction { avatar }
suspend fun UserEntity.posts() = transaction { posts }
suspend fun UserEntity.images() = transaction { images }
suspend fun UserEntity.subscribers() = transaction { subscribers }
suspend fun UserEntity.subscriptions() = transaction { subscriptions }
suspend fun UserEntity.likedPosts() = transaction { likedPosts }

suspend fun UserEntity.subscribedTo(userId: UUID) = transaction {
    subscriptions.any { it.id.value == userId }
}

suspend fun PostEntity.likes() = transaction { likes }

fun UserEntity.toDomainModel(avatar: Image? = null, subscribed: Boolean = false) = User(
    id = id.value,
    username = username,
    displayName = displayName,
    bio = bio,
    avatar = avatar,
    subscribed = subscribed,
)

suspend fun ImageEntity.toDomainModel() = Image(
    id = id.value,
    owner = transaction { owner.toDomainModel() },
    dateCreated = dateCreated,
    width = width,
    height = height,
    url = url,
)

suspend fun PostEntity.toDomainModel(likes: Likes) = Post(
    id = id.value,
    owner = transaction { owner.toDomainModel() },
    dateCreated = dateCreated,
    text = text,
    image = transaction { image?.toDomainModel() },
    likes = likes,
)
