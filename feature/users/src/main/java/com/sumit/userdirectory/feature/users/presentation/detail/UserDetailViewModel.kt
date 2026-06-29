package com.sumit.userdirectory.feature.users.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.userdirectory.core.common.error.AppError
import com.sumit.userdirectory.core.common.result.AppResult
import com.sumit.userdirectory.feature.users.domain.usecase.GetUserDetailUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val getUserDetailUseCase: GetUserDetailUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(UserDetailState())
    val state: StateFlow<UserDetailState> = _state

    private val effects = Channel<UserDetailEffect>(Channel.BUFFERED)
    val effect = effects.receiveAsFlow()

    private var currentUserId: Int? = null

    fun onIntent(intent: UserDetailIntent) {
        when (intent) {
            is UserDetailIntent.LoadUser -> {
                currentUserId = intent.userId
                loadUser(intent.userId)
            }

            UserDetailIntent.Retry -> {
                currentUserId?.let(::loadUser)
            }

            UserDetailIntent.BackClicked -> {
                viewModelScope.launch {
                    effects.send(UserDetailEffect.NavigateBack)
                }
            }
        }
    }

    private fun loadUser(userId: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            }

            when (val result = getUserDetailUseCase(userId)) {
                is AppResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            user = result.data,
                            errorMessage = null,
                        )
                    }
                }

                is AppResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            user = null,
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
