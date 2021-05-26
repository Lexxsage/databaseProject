package com.lexxsage.nanopost.db

import io.ktor.util.date.*
import org.jetbrains.exposed.sql.SizedCollection
import java.util.*

object DAO {
    suspend fun createUser(
        username: String,
        passwordHash: String,
    ) = transaction {
        UserEntity.new {
            this.username = username
            this.passwordHash = passwordHash
            this.passwordUpdated = GMTDate().timestamp
        }
    }

    suspend fun getUser(id: UUID) = transaction {
        UserEntity[id]
    }

    suspend fun findUser(username: String) = transaction {
        UserEntity.find { Users.username eq username }.singleOrNull()
    }

    suspend fun updateUser(
        user: UserEntity,
        username: String?,
        displayName: String?,
        bio: String?,
        avatarId: String?,
    ) = transaction {
        username?.let { user.username = it }
        displayName?.let { user.displayName = it.takeIf { it.isNotBlank() } }
        bio?.let { user.bio = it.takeIf { it.isNotBlank() } }
        avatarId?.let {
            user.avatar = it.takeIf {
                it.isNotBlank()
            }?.let {
                ImageEntity[UUID.fromString(it)]
            }
        }
    }

    suspend fun updateUserPassword(
        user: UserEntity,
        passwordHash: String,
    ) = transaction {
        user.passwordHash = passwordHash
        user.passwordUpdated = GMTDate().timestamp
    }

    suspend fun registerPushToken(
        user: UserEntity,
        pushToken: String,
    ) = transaction {
        user.pushToken = pushToken
    }

    suspend fun unregisterPushToken(user: UserEntity) = transaction {
        user.pushToken = null
    }

    suspend fun subscribe(who: UserEntity, toId: UUID) = transaction {
        val to = UserEntity[toId]
        val subscribedTo = who.subscriptions.toSet()
        who.subscriptions = SizedCollection(subscribedTo + to)
    }

    suspend fun unsubscribe(who: UserEntity, fromId: UUID) = transaction {
        val from = UserEntity[fromId]
        val subscribedTo = who.subscriptions.toSet()
        who.subscriptions = SizedCollection(subscribedTo - from)
    }

    suspend fun createPost(
        owner: UserEntity,
        text: String?,
        imageId: UUID?,
    ) = transaction {
        PostEntity.new {
            this.owner = owner
            this.dateCreated = GMTDate().timestamp
            this.text = text
            this.image = imageId?.let { ImageEntity[it] }
        }
    }

    suspend fun getPost(id: UUID) = transaction {
        PostEntity[id]
    }

    suspend fun createImage(
        id: UUID,
        owner: UserEntity,
        width: Int,
        height: Int,
        url: String,
    ) = transaction {
        ImageEntity.new(id) {
            this.owner = owner
            this.dateCreated = GMTDate().timestamp
            this.width = width
            this.height = height
            this.url = url
        }
    }

    suspend fun getImage(id: UUID) = transaction {
        ImageEntity[id]
    }

    suspend fun likePost(postId: UUID, user: UserEntity) = transaction {
        val post = PostEntity[postId]
        val likes = SizedCollection(post.likes.toSet() + user)
        post.likes = likes
        return@transaction likes
    }

    suspend fun unlikePost(postId: UUID, user: UserEntity) = transaction {
        val post = PostEntity[postId]
        val likes = SizedCollection(post.likes.toSet() - user)
        post.likes = likes
        return@transaction likes
    }
}
