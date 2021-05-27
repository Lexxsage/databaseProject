package com.lexxsage.nanopost.routes

import com.lexxsage.nanopost.db.Timestamped
import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ResultResponse(val result: Boolean = true)

@Serializable
data class PagedDataResponse<T>(
    val count: Int,
    val total: Int,
    val nextFrom: String,
    val items: List<T>,
)

data class NextFrom(val startTime: Long, val offset: Int) {
    companion object {
        val Default = NextFrom(GMTDate().timestamp, 0)
    }
}

fun Parameters.decodeNextFrom(): NextFrom {
    val nextFrom = get("nextFrom")
    return if (nextFrom != null) {
        Base64.getUrlDecoder().decode(nextFrom)
            .toString(Charsets.UTF_8)
            .split("$")
            .let { NextFrom(it[0].toLong(), it[1].toInt()) }
    } else {
        NextFrom.Default
    }
}

fun encodeNextFrom(startTime: Long, offset: Int): String {
    val str = "$startTime$$offset".encodeToByteArray()
    return Base64.getUrlEncoder().encodeToString(str)
}

inline fun <T : Timestamped, R> Iterable<T>.pagedResponse(
    startTime: Long,
    offset: Int,
    count: Int,
    transform: (T) -> R,
): PagedDataResponse<R> {
    val slice = this.sortedByDescending { it.dateCreated }
        .asSequence()
        .dropWhile { it.dateCreated > startTime }
        .drop(offset)
        .take(count)
        .toList()
        .map(transform)
    return PagedDataResponse(
        count = slice.count(),
        total = this.count(),
        nextFrom = encodeNextFrom(startTime, offset + count),
        items = slice,
    )
}
