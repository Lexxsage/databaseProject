package com.lexxsage.nanopost.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import kotlin.text.toCharArray

fun hashPassword(password: String): String {
    return BCrypt.withDefaults().hashToString(8, password.toCharArray())
}

fun verifyPassword(password: String?, passwordHash: String?): Boolean {
    return if (passwordHash != null && password != null) {
        BCrypt.verifyer().verify(password.toCharArray(), passwordHash).verified
    } else {
        false
    }
}
