package com.sumit.userdirectory.feature.users.data.remote

import com.sumit.userdirectory.core.network.NetworkConstants
import com.sumit.userdirectory.feature.users.data.remote.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class KtorUsersRemoteDataSource(
    private val httpClient: HttpClient,
) : UsersRemoteDataSource {
    override suspend fun getUsers(): List<UserDto> {
        return runCatching {
            fetchUsers(NetworkConstants.PRIMARY_USERS_URL)
        }.getOrElse {
            fetchUsers(NetworkConstants.FALLBACK_USERS_URL)
        }
    }

    private suspend fun fetchUsers(url: String): List<UserDto> {
        return httpClient.get(url).body()
    }
}
