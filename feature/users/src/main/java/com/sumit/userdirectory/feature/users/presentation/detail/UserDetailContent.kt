package com.sumit.userdirectory.feature.users.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sumit.userdirectory.feature.users.domain.model.User
import com.sumit.userdirectory.feature.users.presentation.components.ErrorView
import com.sumit.userdirectory.feature.users.presentation.components.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailContent(
    state: UserDetailState,
    onIntent: (UserDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.testTag("user_detail_screen"),
        topBar = {
            TopAppBar(
                title = { Text(text = "User Details") },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.testTag("user_detail_back_button"),
                        onClick = { onIntent(UserDetailIntent.BackClicked) },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when {
            state.isLoading -> LoadingView(
                testTag = "user_detail_loading",
                modifier = Modifier.padding(innerPadding),
            )

            state.errorMessage != null -> ErrorView(
                message = state.errorMessage,
                onRetryClick = { onIntent(UserDetailIntent.Retry) },
                errorTestTag = "user_detail_error",
                retryButtonTestTag = "user_detail_retry_button",
                modifier = Modifier.padding(innerPadding),
            )

            state.user != null -> UserDetailBody(
                user = state.user,
                contentPadding = innerPadding,
            )
        }
    }
}

@Composable
private fun UserDetailBody(
    user: User,
    contentPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        DetailSection {
            Text(
                modifier = Modifier.testTag("user_detail_name"),
                text = user.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "@${user.username}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        DetailSection {
            DetailRow(
                label = "Email",
                value = user.email,
                testTag = "user_detail_email",
            )
            DetailRow(
                label = "Phone",
                value = user.phone,
                testTag = "user_detail_phone",
            )
            DetailRow(
                label = "Website",
                value = user.website,
            )
        }

        DetailSection {
            DetailRow(
                label = "Company",
                value = user.company.name,
                testTag = "user_detail_company",
            )
            DetailRow(
                label = "Catch phrase",
                value = user.company.catchPhrase,
            )
        }

        DetailSection {
            DetailRow(
                label = "Address",
                value = user.address.fullAddress,
                testTag = "user_detail_address",
            )
        }
    }
}

@Composable
private fun DetailSection(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content,
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    testTag: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value.ifBlank { "-" },
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
