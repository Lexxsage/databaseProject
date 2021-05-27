package com.lexxsage.nanopost.db

import com.lexxsage.nanopost.uuid
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

    suspend fun getUser(id: String) = transaction {
        UserEntity[id.uuid()]
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
            }?.let { id ->
                ImageEntity[id.uuid()]
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

    suspend fun subscribe(who: UserEntity, toId: String) = transaction {
        val to = UserEntity[toId.uuid()]
        val subscribedTo = who.subscriptions.toSet()
        who.subscriptions = SizedCollection(subscribedTo + to)
    }

    suspend fun unsubscribe(who: UserEntity, fromId: String) = transaction {
        val from = UserEntity[fromId.uuid()]
        val subscribedTo = who.subscriptions.toSet()
        who.subscriptions = SizedCollection(subscribedTo - from)
    }

    suspend fun createPost(
        owner: UserEntity,
        text: String?,
        imageId: String?,
    ) = transaction {
        PostEntity.new {
            this.owner = owner
            this.dateCreated = GMTDate().timestamp
            this.text = text
            this.image = imageId?.let { ImageEntity[it.uuid()] }
        }
    }

    suspend fun getPost(id: String) = transaction {
        PostEntity[id.uuid()]
    }

    suspend fun createImage(
        id: String,
        owner: UserEntity,
        width: Int,
        height: Int,
        url: String,
    ) = transaction {
        ImageEntity.new(id.uuid()) {
            this.owner = owner
            this.dateCreated = GMTDate().timestamp
            this.width = width
            this.height = height
            this.url = url
        }
    }

    suspend fun getImage(id: String) = transaction {
        ImageEntity[id.uuid()]
    }

    suspend fun likePost(postId: String, user: UserEntity) = transaction {
        val post = PostEntity[postId.uuid()]
        val likes = SizedCollection(post.likes.toSet() + user)
        post.likes = likes
        return@transaction likes
    }

    suspend fun unlikePost(postId: String, user: UserEntity) = transaction {
        val post = PostEntity[postId.uuid()]
        val likes = SizedCollection(post.likes.toSet() - user)
        post.likes = likes
        return@transaction likes
    }
}
