package com.lexxsage.nanopost.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend inline fun <T> transaction(
    noinline block: suspend Transaction.() -> T,
): T = newSuspendedTransaction(Dispatchers.IO, statement = block)
