package com.sumit.userdirectory.feature.users.presentation.list

import app.cash.turbine.test
import com.sumit.userdirectory.core.common.error.AppError
import com.sumit.userdirectory.core.common.result.AppResult
import com.sumit.userdirectory.core.testing.MainDispatcherRule
import com.sumit.userdirectory.feature.users.domain.model.Address
import com.sumit.userdirectory.feature.users.domain.model.Company
import com.sumit.userdirectory.feature.users.domain.model.User
import com.sumit.userdirectory.feature.users.domain.repository.UsersRepository
import com.sumit.userdirectory.feature.users.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    @Test
    fun `initial load shows loading then success`() = runTest {
        val usersResult = CompletableDeferred<AppResult<List<User>>>()
        val viewModel = viewModelWith(
            repository = FakeUsersRepository(usersResults = mutableListOf(usersResult)),
        )

        viewModel.state.test {
            assertEquals(UserListState(), awaitItem())

            runCurrent()
            assertEquals(UserListState(isLoading = true), awaitItem())

            val users = listOf(testUser(id = 1))
            usersResult.complete(AppResult.Success(users))
            advanceUntilIdle()

            assertEquals(UserListState(users = users), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `failed load shows error`() = runTest {
        val usersResult = CompletableDeferred<AppResult<List<User>>>()
        val viewModel = viewModelWith(
            repository = FakeUsersRepository(usersResults = mutableListOf(usersResult)),
        )

        viewModel.state.test {
            awaitItem()
            runCurrent()
            assertEquals(UserListState(isLoading = true), awaitItem())

            usersResult.complete(AppResult.Error(AppError.Network("Network unavailable")))
            advanceUntilIdle()

            assertEquals(UserListState(errorMessage = "Network unavailable"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry loads users again`() = runTest {
        val firstResult = CompletableDeferred<AppResult<List<User>>>()
        val secondResult = CompletableDeferred<AppResult<List<User>>>()
        val repository = FakeUsersRepository(
            usersResults = mutableListOf(firstResult, secondResult),
        )
        val viewModel = viewModelWith(repository = repository)

        viewModel.state.test {
            awaitItem()
            runCurrent()
            awaitItem()

            firstResult.complete(AppResult.Error(AppError.Network("Network unavailable")))
            advanceUntilIdle()
            assertEquals(UserListState(errorMessage = "Network unavailable"), awaitItem())

            viewModel.onIntent(UserListIntent.Retry)
            runCurrent()
            assertEquals(UserListState(isLoading = true), awaitItem())

            val users = listOf(testUser(id = 2))
            secondResult.complete(AppResult.Success(users))
            advanceUntilIdle()

            assertEquals(UserListState(users = users), awaitItem())
            assertEquals(2, repository.getUsersCallCount)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `user click emits NavigateToDetail effect`() = runTest {
        val viewModel = viewModelWith(
            repository = FakeUsersRepository(
                usersResults = mutableListOf(CompletableDeferred(AppResult.Success(emptyList()))),
            ),
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onIntent(UserListIntent.UserClicked(userId = 7))

            assertEquals(UserListEffect.NavigateToDetail(userId = 7), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun viewModelWith(repository: UsersRepository): UserListViewModel {
        return UserListViewModel(
            getUsersUseCase = GetUsersUseCase(repository),
        )
    }
}

private class FakeUsersRepository(
    private val usersResults: MutableList<CompletableDeferred<AppResult<List<User>>>>,
) : UsersRepository {
    var getUsersCallCount: Int = 0
        private set

    override suspend fun getUsers(): AppResult<List<User>> {
        getUsersCallCount += 1
        return usersResults.removeAt(0).await()
    }

    override suspend fun getUserById(userId: Int): AppResult<User> {
        return AppResult.Error(AppError.NotFound("User not found"))
    }
}

private fun testUser(id: Int): User {
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
