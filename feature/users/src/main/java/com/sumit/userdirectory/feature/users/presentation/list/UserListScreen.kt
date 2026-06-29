package com.sumit.userdirectory.feature.users.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserListScreen(
    onUserClick: (Int) -> Unit,
    viewModel: UserListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is UserListEffect.NavigateToDetail -> onUserClick(effect.userId)
            }
        }
    }

    UserListContent(
        state = state,
        onIntent = viewModel::onIntent,
    )
}
