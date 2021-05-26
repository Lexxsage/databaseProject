@file:UseSerializers(UUIDSerializer::class)
package com.lexxsage.nanopost

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

class UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())
}

@Serializable
data class User(
    val id: UUID,
    val username: String,
    val displayName: String?,
    val bio: String?,
    val avatar: Image? = null,
    val subscribed: Boolean = false,
)

@Serializable
data class Image(
    val id: UUID,
    val owner: User,
    val dateCreated: Long,
    val width: Int,
    val height: Int,
    val url: String,
)

@Serializable
data class Post(
    val id: UUID,
    val owner: User,
    val dateCreated: Long,
    val text: String?,
    val image: Image?,
    var likes: Likes? = null,
)

@Serializable
data class Likes(
    val liked: Boolean,
    val likesCount: Int,
) {
    companion object {
        val Default = Likes(false, 0)
    }
}
