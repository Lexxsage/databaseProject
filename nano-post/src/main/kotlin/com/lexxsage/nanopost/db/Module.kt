package com.lexxsage.nanopost.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils

fun initDatabase(testing: Boolean): Database {
    val db = Database.connect(
        url = "jdbc:postgresql://localhost:5432/evolitist",
        driver = "org.postgresql.Driver",
        user = "evolitist",
    )

    org.jetbrains.exposed.sql.transactions.transaction(db) {
        SchemaUtils.createMissingTablesAndColumns(Users, Images, Posts, PostsLikes, Subscriptions, inBatch = true)
    }

    return db
}
