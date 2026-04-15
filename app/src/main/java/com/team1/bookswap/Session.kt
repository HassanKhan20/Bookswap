package com.team1.bookswap

import com.team1.bookswap.db.User

object Session {
    var currentUser: User? = null
    val userId: Int get() = currentUser?.id ?: -1
    fun isLoggedIn(): Boolean = currentUser != null
    fun logout() { currentUser = null }
}
