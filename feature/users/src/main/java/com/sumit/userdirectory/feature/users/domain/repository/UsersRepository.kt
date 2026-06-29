package com.sumit.userdirectory.feature.users.domain.repository

import com.sumit.userdirectory.core.common.result.AppResult
import com.sumit.userdirectory.feature.users.domain.model.User

interface UsersRepository {
    suspend fun getUsers(): AppResult<List<User>>
    suspend fun getUserById(userId: Int): AppResult<User>
}
