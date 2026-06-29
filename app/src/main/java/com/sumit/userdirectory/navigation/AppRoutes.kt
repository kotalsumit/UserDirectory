package com.sumit.userdirectory.navigation

object AppRoutes {
    const val USERS = "users"
    const val USER_DETAIL = "users/{userId}"
    const val USER_ID_ARGUMENT = "userId"

    fun userDetail(userId: Int): String = "users/$userId"
}
