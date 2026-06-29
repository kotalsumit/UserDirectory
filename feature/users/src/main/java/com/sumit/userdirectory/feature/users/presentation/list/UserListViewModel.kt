package com.sumit.userdirectory.feature.users.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.userdirectory.core.common.error.AppError
import com.sumit.userdirectory.core.common.result.AppResult
import com.sumit.userdirectory.feature.users.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserListViewModel(
    private val getUsersUseCase: GetUsersUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(UserListState())
    val state: StateFlow<UserListState> = _state

    private val effects = Channel<UserListEffect>(Channel.BUFFERED)
    val effect = effects.receiveAsFlow()

    init {
        onIntent(UserListIntent.LoadUsers)
    }

    fun onIntent(intent: UserListIntent) {
        when (intent) {
            UserListIntent.LoadUsers,
            UserListIntent.Retry,
            -> loadUsers()

            is UserListIntent.UserClicked -> {
                viewModelScope.launch {
                    effects.send(UserListEffect.NavigateToDetail(intent.userId))
                }
            }
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            }

            when (val result = getUsersUseCase()) {
                is AppResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            users = result.data,
                            errorMessage = null,
                        )
                    }
                }

                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            users = emptyList(),
                            errorMessage = result.error.message(),
                        )
                    }
                }
            }
        }
    }

    private fun AppError.message(): String {
        return when (this) {
            is AppError.Network -> message
            is AppError.NotFound -> message
            is AppError.Unknown -> message
        }
    }
}
