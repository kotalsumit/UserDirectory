package com.sumit.userdirectory.feature.users.presentation.detail

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.sumit.userdirectory.feature.users.domain.model.Address
import com.sumit.userdirectory.feature.users.domain.model.Company
import com.sumit.userdirectory.feature.users.domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class UserDetailContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loadingStateShowsProgressIndicator() {
        composeRule.setContent {
            MaterialTheme {
                UserDetailContent(
                    state = UserDetailState(isLoading = true),
                    onIntent = {},
                )
            }
        }

        composeRule.onNodeWithTag("user_detail_loading").assertIsDisplayed()
    }

    @Test
    fun successStateShowsUserDetails() {
        composeRule.setContent {
            MaterialTheme {
                UserDetailContent(
                    state = UserDetailState(user = sampleUser()),
                    onIntent = {},
                )
            }
        }

        composeRule.onNodeWithTag("user_detail_name").assertIsDisplayed()
        composeRule.onNodeWithTag("user_detail_email").assertIsDisplayed()
        composeRule.onNodeWithTag("user_detail_phone").assertIsDisplayed()
        composeRule.onNodeWithTag("user_detail_company").assertIsDisplayed()
        composeRule.onNodeWithTag("user_detail_address").assertIsDisplayed()
        composeRule.onNodeWithText("Leanne Graham").assertIsDisplayed()
        composeRule.onNodeWithText("Sincere@april.biz").assertIsDisplayed()
        composeRule.onNodeWithText("Romaguera-Crona").assertIsDisplayed()
    }

    @Test
    fun errorStateShowsRetryButton() {
        var capturedIntent: UserDetailIntent? = null

        composeRule.setContent {
            MaterialTheme {
                UserDetailContent(
                    state = UserDetailState(errorMessage = "Unable to load user"),
                    onIntent = { capturedIntent = it },
                )
            }
        }

        composeRule.onNodeWithTag("user_detail_error").assertIsDisplayed()
        composeRule.onNodeWithTag("user_detail_retry_button")
            .assertIsDisplayed()
            .performClick()

        assertEquals(UserDetailIntent.Retry, capturedIntent)
    }

    @Test
    fun backButtonCallsBackClickedIntent() {
        var capturedIntent: UserDetailIntent? = null

        composeRule.setContent {
            MaterialTheme {
                UserDetailContent(
                    state = UserDetailState(user = sampleUser()),
                    onIntent = { capturedIntent = it },
                )
            }
        }

        composeRule.onNodeWithTag("user_detail_back_button")
            .assertIsDisplayed()
            .performClick()

        assertEquals(UserDetailIntent.BackClicked, capturedIntent)
    }

    @Test
    fun backButtonHasAccessibleLabel() {
        composeRule.setContent {
            MaterialTheme {
                UserDetailContent(
                    state = UserDetailState(user = sampleUser()),
                    onIntent = {},
                )
            }
        }

        composeRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    private fun sampleUser(): User = User(
        id = 1,
        name = "Leanne Graham",
        username = "Bret",
        email = "Sincere@april.biz",
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
            name = "Romaguera-Crona",
            catchPhrase = "Multi-layered client-server neural-net",
        ),
    )
}
