package com.sumit.userdirectory.feature.users.data.repository

import com.sumit.userdirectory.core.common.error.AppError
import com.sumit.userdirectory.core.common.result.AppResult
import com.sumit.userdirectory.core.testing.TestDispatcherProvider
import com.sumit.userdirectory.feature.users.data.remote.UsersRemoteDataSource
import com.sumit.userdirectory.feature.users.data.remote.dto.AddressDto
import com.sumit.userdirectory.feature.users.data.remote.dto.CompanyDto
import com.sumit.userdirectory.feature.users.data.remote.dto.UserDto
import com.sumit.userdirectory.feature.users.domain.model.User
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UsersRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()

    @Test
    fun `returns mapped users when remote data source succeeds`() = runTest(testDispatcher) {
        val repository = repositoryWith(
            remoteDataSource = FakeUsersRemoteDataSource(users = listOf(userDto(id = 1))),
        )

        val result = repository.getUsers()

        val success = assertIs<AppResult.Success<List<User>>>(result)
        assertEquals(1, success.data.size)
        assertEquals(1, success.data.first().id)
        assertEquals("Leanne Graham", success.data.first().name)
        assertEquals("Kulas Light, Apt. 556, Gwenborough 92998-3874", success.data.first().address.fullAddress)
    }

    @Test
    fun `returns error when remote data source fails`() = runTest(testDispatcher) {
        val repository = repositoryWith(
            remoteDataSource = FakeUsersRemoteDataSource(error = IllegalStateException("Network unavailable")),
        )

        val result = repository.getUsers()

        val failure = assertIs<AppResult.Error>(result)
        val error = assertIs<AppError.Network>(failure.error)
        assertEquals("Network unavailable", error.message)
    }

    @Test
    fun `caches users after successful fetch`() = runTest(testDispatcher) {
        val remoteDataSource = FakeUsersRemoteDataSource(users = listOf(userDto(id = 1)))
        val repository = repositoryWith(remoteDataSource = remoteDataSource)

        repository.getUsers()
        val result = repository.getUserById(userId = 1)

        assertIs<AppResult.Success<User>>(result)
        assertEquals(1, remoteDataSource.callCount)
    }

    @Test
    fun `getUserById returns user from cache`() = runTest(testDispatcher) {
        val remoteDataSource = FakeUsersRemoteDataSource(users = listOf(userDto(id = 2)))
        val repository = repositoryWith(remoteDataSource = remoteDataSource)

        repository.getUsers()
        val result = repository.getUserById(userId = 2)

        val success = assertIs<AppResult.Success<User>>(result)
        assertEquals(2, success.data.id)
        assertEquals(1, remoteDataSource.callCount)
    }

    @Test
    fun `getUserById fetches users if cache is empty`() = runTest(testDispatcher) {
        val remoteDataSource = FakeUsersRemoteDataSource(users = listOf(userDto(id = 3)))
        val repository = repositoryWith(remoteDataSource = remoteDataSource)

        val result = repository.getUserById(userId = 3)

        val success = assertIs<AppResult.Success<User>>(result)
        assertEquals(3, success.data.id)
        assertEquals(1, remoteDataSource.callCount)
    }

    private fun repositoryWith(remoteDataSource: UsersRemoteDataSource): UsersRepositoryImpl {
        return UsersRepositoryImpl(
            remoteDataSource = remoteDataSource,
            dispatcherProvider = TestDispatcherProvider(testDispatcher),
        )
    }
}

private class FakeUsersRemoteDataSource(
    private val users: List<UserDto> = emptyList(),
    private val error: Throwable? = null,
) : UsersRemoteDataSource {
    var callCount: Int = 0
        private set

    override suspend fun getUsers(): List<UserDto> {
        callCount += 1
        error?.let { throw it }
        return users
    }
}

private fun userDto(id: Int): UserDto {
    return UserDto(
        id = id,
        name = "Leanne Graham",
        username = "Bret",
        email = "leanne@example.com",
        phone = "1-770-736-8031",
        website = "hildegard.org",
        address = AddressDto(
            street = "Kulas Light",
            suite = "Apt. 556",
            city = "Gwenborough",
            zipcode = "92998-3874",
        ),
        company = CompanyDto(
            name = "Romaguera-Crona",
            catchPhrase = "Multi-layered client-server neural-net",
            bs = "harness real-time e-markets",
        ),
    )
}
