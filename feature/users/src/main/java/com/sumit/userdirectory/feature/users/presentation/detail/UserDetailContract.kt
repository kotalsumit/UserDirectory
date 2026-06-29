package com.sumit.userdirectory.feature.users.presentation.detail

import com.sumit.userdirectory.feature.users.domain.model.User

data class UserDetailState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
)

sealed interface UserDetailIntent {
    data class LoadUser(val userId: Int) : UserDetailIntent
    data object Retry : UserDetailIntent
    data object BackClicked : UserDetailIntent
}

sealed interface UserDetailEffect {
    data object NavigateBack : UserDetailEffect
}
