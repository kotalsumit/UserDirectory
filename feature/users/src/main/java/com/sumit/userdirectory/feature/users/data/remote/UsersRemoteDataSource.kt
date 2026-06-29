package com.sumit.userdirectory.feature.users.data.remote

import com.sumit.userdirectory.feature.users.data.remote.dto.UserDto

interface UsersRemoteDataSource {
    suspend fun getUsers(): List<UserDto>
}
