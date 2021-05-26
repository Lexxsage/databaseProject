package com.lexxsage.nanopost.db

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Users : UUIDTable("users") {
    val username = varchar("username", 16).uniqueIndex()
    val passwordHash = varchar("passwordHash", 64)
    val passwordUpdated = long("passwordUpdated")
    val pushToken = varchar("pushToken", 164).nullable()
    val displayName = varchar("displayName", 64).nullable()
    val bio = varchar("bio", 280).nullable()
    val avatar = optReference("avatar", Images, onDelete = ReferenceOption.SET_NULL)
}

object Images : UUIDTable("images") {
    val owner = reference("owner", Users, onDelete = ReferenceOption.CASCADE)
    val dateCreated = long("dateCreated")
    val width = integer("width")
    val height = integer("height")
    val url = varchar("url", 256)
}

object Posts : UUIDTable("posts") {
    val owner = reference("owner", Users, onDelete = ReferenceOption.CASCADE)
    val dateCreated = long("dateCreated")
    val text = varchar("text", 1024).nullable()
    val image = optReference("image", Images, onDelete = ReferenceOption.SET_NULL)
}

object PostsLikes : Table("posts_likes") {
    val post = reference("post", Posts, onDelete = ReferenceOption.CASCADE)
    val user = reference("user", Users, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(post, user)
}

object Subscriptions : Table("subscriptions") {
    val user = reference("user", Users, onDelete = ReferenceOption.CASCADE)
    val subscriber = reference("subscriber", Users, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(user, subscriber)
}
