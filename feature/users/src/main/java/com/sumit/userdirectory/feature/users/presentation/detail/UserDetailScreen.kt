package com.sumit.userdirectory.feature.users.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDetailScreen(
    userId: Int,
    onBackClick: () -> Unit,
    viewModel: UserDetailViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.onIntent(UserDetailIntent.LoadUser(userId))
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                UserDetailEffect.NavigateBack -> onBackClick()
            }
        }
    }

    UserDetailContent(
        state = state,
        onIntent = viewModel::onIntent,
    )
}
