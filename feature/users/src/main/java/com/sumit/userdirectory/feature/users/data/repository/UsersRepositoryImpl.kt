package com.sumit.userdirectory.feature.users.data.repository

import com.sumit.userdirectory.core.common.coroutine.DispatcherProvider
import com.sumit.userdirectory.core.common.error.AppError
import com.sumit.userdirectory.core.common.result.AppResult
import com.sumit.userdirectory.feature.users.data.mapper.UserDtoMapper
import com.sumit.userdirectory.feature.users.data.remote.UsersRemoteDataSource
import com.sumit.userdirectory.feature.users.domain.model.User
import com.sumit.userdirectory.feature.users.domain.repository.UsersRepository
import kotlinx.coroutines.withContext

class UsersRepositoryImpl(
    private val remoteDataSource: UsersRemoteDataSource,
    private val dispatcherProvider: DispatcherProvider,
) : UsersRepository {
    private var cachedUsers: List<User> = emptyList()

    override suspend fun getUsers(): AppResult<List<User>> {
        return fetchUsers()
    }

    override suspend fun getUserById(userId: Int): AppResult<User> {
        val cachedUser = cachedUsers.firstOrNull { it.id == userId }
        if (cachedUser != null) {
            return AppResult.Success(cachedUser)
        }

        return when (val usersResult = fetchUsers()) {
            is AppResult.Success -> {
                val user = usersResult.data.firstOrNull { it.id == userId }
                if (user != null) {
                    AppResult.Success(user)
                } else {
                    AppResult.Error(AppError.NotFound("User not found"))
                }
            }

            is AppResult.Error -> usersResult
        }
    }

    private suspend fun fetchUsers(): AppResult<List<User>> {
        return withContext(dispatcherProvider.io) {
            runCatching {
                remoteDataSource.getUsers().map(UserDtoMapper::map)
            }.fold(
                onSuccess = { users ->
                    cachedUsers = users
                    AppResult.Success(users)
                },
                onFailure = { throwable ->
                    AppResult.Error(AppError.Network(throwable.message ?: "Unable to load users"))
                },
            )
        }
    }
}
