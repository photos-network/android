package photos.network.home.photos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test
import photos.network.MainActivity
import photos.network.theme.AppTheme

class PhotosScreenTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loading_spinner_should_be_shown_while_loading() {
        // given
        val uiState = PhotosUiState(isLoading = true)

        // when
        composeTestRule.setContent {
            AppTheme {
                PhotosContent(uiState = uiState, handleEvent = {})
            }
        }

        // then
        composeTestRule.onNodeWithTag("LOADING_SPINNER").assertIsDisplayed()
    }
}
