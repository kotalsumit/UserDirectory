package com.sumit.userdirectory.feature.users.presentation.list

import com.sumit.userdirectory.feature.users.domain.model.User

data class UserListState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val errorMessage: String? = null,
)

sealed interface UserListIntent {
    data object LoadUsers : UserListIntent
    data object Retry : UserListIntent
    data class UserClicked(val userId: Int) : UserListIntent
}

sealed interface UserListEffect {
    data class NavigateToDetail(val userId: Int) : UserListEffect
}
