package com.sumit.userdirectory.feature.users.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.sumit.userdirectory.feature.users.presentation.components.EmptyView
import com.sumit.userdirectory.feature.users.presentation.components.ErrorView
import com.sumit.userdirectory.feature.users.presentation.components.LoadingView
import com.sumit.userdirectory.feature.users.presentation.components.UserCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListContent(
    state: UserListState,
    onIntent: (UserListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.testTag("user_list_screen"),
        topBar = {
            TopAppBar(title = { Text(text = "Users") })
        },
    ) { innerPadding ->
        when {
            state.isLoading -> LoadingView(
                testTag = "user_list_loading",
                modifier = Modifier.padding(innerPadding),
            )

            state.errorMessage != null -> ErrorView(
                message = state.errorMessage,
                onRetryClick = { onIntent(UserListIntent.Retry) },
                errorTestTag = "user_list_error",
                retryButtonTestTag = "user_list_retry_button",
                modifier = Modifier.padding(innerPadding),
            )

            state.users.isEmpty() -> EmptyView(
                message = "No users found",
                testTag = "user_list_empty",
                modifier = Modifier.padding(innerPadding),
            )

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = state.users,
                    key = { user -> user.id },
                ) { user ->
                    UserCard(
                        user = user,
                        onClick = { onIntent(UserListIntent.UserClicked(user.id)) },
                    )
                }
            }
        }
    }
}
