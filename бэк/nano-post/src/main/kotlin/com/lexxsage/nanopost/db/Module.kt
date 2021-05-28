package com.lexxsage.nanopost.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils

fun initDatabase(testing: Boolean): Database {
    val db = Database.connect(
        url = "jdbc:postgresql://localhost:5432/u191200",
        driver = "org.postgresql.Driver",
        user = "u191200",
        password = "Lexxsage18"
    )

    org.jetbrains.exposed.sql.transactions.transaction(db) {
        SchemaUtils.createMissingTablesAndColumns(Users, Images, Posts, PostsLikes, Subscriptions, inBatch = true)
    }

    return db
}
