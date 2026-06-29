package com.sumit.userdirectory.feature.users.presentation.list

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.sumit.userdirectory.feature.users.domain.model.Address
import com.sumit.userdirectory.feature.users.domain.model.Company
import com.sumit.userdirectory.feature.users.domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class UserListContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loadingStateShowsProgressIndicator() {
        composeRule.setContent {
            MaterialTheme {
                UserListContent(
                    state = UserListState(isLoading = true),
                    onIntent = {},
                )
            }
        }

        composeRule.onNodeWithTag("user_list_loading").assertIsDisplayed()
    }

    @Test
    fun successStateShowsUserCards() {
        composeRule.setContent {
            MaterialTheme {
                UserListContent(
                    state = UserListState(users = sampleUsers()),
                    onIntent = {},
                )
            }
        }

        composeRule.onNodeWithTag("user_card_1").assertIsDisplayed()
        composeRule.onNodeWithTag("user_name_1", useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithTag("user_email_1", useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithText("Leanne Graham").assertIsDisplayed()
        composeRule.onNodeWithText("Sincere@april.biz").assertIsDisplayed()
    }

    @Test
    fun errorStateShowsRetryButton() {
        var capturedIntent: UserListIntent? = null

        composeRule.setContent {
            MaterialTheme {
                UserListContent(
                    state = UserListState(errorMessage = "Unable to load users"),
                    onIntent = { capturedIntent = it },
                )
            }
        }

        composeRule.onNodeWithTag("user_list_error").assertIsDisplayed()
        composeRule.onNodeWithTag("user_list_retry_button")
            .assertIsDisplayed()
            .performClick()

        assertEquals(UserListIntent.Retry, capturedIntent)
    }

    @Test
    fun emptyStateShowsEmptyMessage() {
        composeRule.setContent {
            MaterialTheme {
                UserListContent(
                    state = UserListState(users = emptyList()),
                    onIntent = {},
                )
            }
        }

        composeRule.onNodeWithTag("user_list_empty").assertIsDisplayed()
        composeRule.onNodeWithText("No users found").assertIsDisplayed()
    }

    @Test
    fun clickingUserCardCallsUserClickedIntent() {
        var capturedIntent: UserListIntent? = null

        composeRule.setContent {
            MaterialTheme {
                UserListContent(
                    state = UserListState(users = sampleUsers()),
                    onIntent = { capturedIntent = it },
                )
            }
        }

        composeRule.onNodeWithTag("user_card_1").performClick()

        assertEquals(UserListIntent.UserClicked(userId = 1), capturedIntent)
    }

    private fun sampleUsers(): List<User> = listOf(
        sampleUser(
            id = 1,
            name = "Leanne Graham",
            username = "Bret",
            email = "Sincere@april.biz",
            companyName = "Romaguera-Crona",
        ),
        sampleUser(
            id = 2,
            name = "Ervin Howell",
            username = "Antonette",
            email = "Shanna@melissa.tv",
            companyName = "Deckow-Crist",
        ),
    )

    private fun sampleUser(
        id: Int,
        name: String,
        username: String,
        email: String,
        companyName: String,
    ): User = User(
        id = id,
        name = name,
        username = username,
        email = email,
        phone = "1-770-736-8031 x56442",
        website = "hildegard.org",
        address = Address(
            street = "Kulas Light",
            suite = "Apt. 556",
            city = "Gwenborough",
            zipcode = "92998-3874",
            fullAddress = "Kulas Light, Apt. 556, Gwenborough 92998-3874",
        ),
        company = Company(
            name = companyName,
            catchPhrase = "Multi-layered client-server neural-net",
        ),
    )
}
