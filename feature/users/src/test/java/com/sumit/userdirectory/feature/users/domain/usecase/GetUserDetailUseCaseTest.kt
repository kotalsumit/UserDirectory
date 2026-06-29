package com.sumit.userdirectory.feature.users.domain.usecase

import com.sumit.userdirectory.core.common.error.AppError
import com.sumit.userdirectory.core.common.result.AppResult
import com.sumit.userdirectory.feature.users.domain.model.Address
import com.sumit.userdirectory.feature.users.domain.model.Company
import com.sumit.userdirectory.feature.users.domain.model.User
import com.sumit.userdirectory.feature.users.domain.repository.UsersRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GetUserDetailUseCaseTest {
    @Test
    fun `returns user when user exists`() = runTest {
        val user = testUser(id = 2)
        val useCase = GetUserDetailUseCase(
            usersRepository = FakeGetUserDetailRepository(userResult = AppResult.Success(user)),
        )

        val result = useCase(userId = 2)

        val success = assertIs<AppResult.Success<User>>(result)
        assertEquals(user, success.data)
    }

    @Test
    fun `returns not found error when user does not exist`() = runTest {
        val error = AppError.NotFound("User not found")
        val useCase = GetUserDetailUseCase(
            usersRepository = FakeGetUserDetailRepository(userResult = AppResult.Error(error)),
        )

        val result = useCase(userId = 99)

        val failure = assertIs<AppResult.Error>(result)
        assertEquals(error, failure.error)
    }
}

private class FakeGetUserDetailRepository(
    private val usersResult: AppResult<List<User>> = AppResult.Success(emptyList()),
    private val userResult: AppResult<User> = AppResult.Error(AppError.NotFound("User not found")),
) : UsersRepository {
    override suspend fun getUsers(): AppResult<List<User>> = usersResult

    override suspend fun getUserById(userId: Int): AppResult<User> = userResult
}

private fun testUser(id: Int = 1): User {
    return User(
        id = id,
        name = "Leanne Graham",
        username = "Bret",
        email = "leanne@example.com",
        phone = "1-770-736-8031",
        website = "hildegard.org",
        address = Address(
            street = "Kulas Light",
            suite = "Apt. 556",
            city = "Gwenborough",
            zipcode = "92998-3874",
            fullAddress = "Kulas Light, Apt. 556, Gwenborough 92998-3874",
        ),
        company = Company(
            name = "Romaguera-Crona",
            catchPhrase = "Multi-layered client-server neural-net",
        ),
    )
}
