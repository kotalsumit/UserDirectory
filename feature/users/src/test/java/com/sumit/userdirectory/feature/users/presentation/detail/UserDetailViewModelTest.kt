package com.sumit.userdirectory.feature.users.presentation.detail

import app.cash.turbine.test
import com.sumit.userdirectory.core.common.error.AppError
import com.sumit.userdirectory.core.common.result.AppResult
import com.sumit.userdirectory.core.testing.MainDispatcherRule
import com.sumit.userdirectory.feature.users.domain.model.Address
import com.sumit.userdirectory.feature.users.domain.model.Company
import com.sumit.userdirectory.feature.users.domain.model.User
import com.sumit.userdirectory.feature.users.domain.repository.UsersRepository
import com.sumit.userdirectory.feature.users.domain.usecase.GetUserDetailUseCase
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
class UserDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    @Test
    fun `load user shows loading then user detail`() = runTest {
        val detailResult = CompletableDeferred<AppResult<User>>()
        val viewModel = viewModelWith(
            repository = FakeUsersRepository(detailResults = mutableListOf(detailResult)),
        )

        viewModel.state.test {
            assertEquals(UserDetailState(), awaitItem())

            viewModel.onIntent(UserDetailIntent.LoadUser(userId = 1))
            runCurrent()
            assertEquals(UserDetailState(isLoading = true), awaitItem())

            val user = testUser(id = 1)
            detailResult.complete(AppResult.Success(user))
            advanceUntilIdle()

            assertEquals(UserDetailState(user = user), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `failed load shows error`() = runTest {
        val detailResult = CompletableDeferred<AppResult<User>>()
        val viewModel = viewModelWith(
            repository = FakeUsersRepository(detailResults = mutableListOf(detailResult)),
        )

        viewModel.state.test {
            awaitItem()

            viewModel.onIntent(UserDetailIntent.LoadUser(userId = 1))
            runCurrent()
            assertEquals(UserDetailState(isLoading = true), awaitItem())

            detailResult.complete(AppResult.Error(AppError.Network("Network unavailable")))
            advanceUntilIdle()

            assertEquals(UserDetailState(errorMessage = "Network unavailable"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry reloads same user`() = runTest {
        val firstResult = CompletableDeferred<AppResult<User>>()
        val secondResult = CompletableDeferred<AppResult<User>>()
        val repository = FakeUsersRepository(
            detailResults = mutableListOf(firstResult, secondResult),
        )
        val viewModel = viewModelWith(repository = repository)

        viewModel.state.test {
            awaitItem()

            viewModel.onIntent(UserDetailIntent.LoadUser(userId = 5))
            runCurrent()
            awaitItem()

            firstResult.complete(AppResult.Error(AppError.Network("Network unavailable")))
            advanceUntilIdle()
            assertEquals(UserDetailState(errorMessage = "Network unavailable"), awaitItem())

            viewModel.onIntent(UserDetailIntent.Retry)
            runCurrent()
            assertEquals(UserDetailState(isLoading = true), awaitItem())

            val user = testUser(id = 5)
            secondResult.complete(AppResult.Success(user))
            advanceUntilIdle()

            assertEquals(UserDetailState(user = user), awaitItem())
            assertEquals(listOf(5, 5), repository.requestedUserIds)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `back click emits NavigateBack effect`() = runTest {
        val viewModel = viewModelWith(
            repository = FakeUsersRepository(detailResults = mutableListOf()),
        )

        viewModel.effect.test {
            viewModel.onIntent(UserDetailIntent.BackClicked)

            assertEquals(UserDetailEffect.NavigateBack, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun viewModelWith(repository: UsersRepository): UserDetailViewModel {
        return UserDetailViewModel(
            getUserDetailUseCase = GetUserDetailUseCase(repository),
        )
    }
}

private class FakeUsersRepository(
    private val detailResults: MutableList<CompletableDeferred<AppResult<User>>>,
) : UsersRepository {
    val requestedUserIds = mutableListOf<Int>()

    override suspend fun getUsers(): AppResult<List<User>> {
        return AppResult.Success(emptyList())
    }

    override suspend fun getUserById(userId: Int): AppResult<User> {
        requestedUserIds += userId
        return detailResults.removeAt(0).await()
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
