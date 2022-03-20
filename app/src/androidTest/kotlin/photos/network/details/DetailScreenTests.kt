package photos.network.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import photos.network.MainActivity
import photos.network.theme.AppTheme

class DetailScreenTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Ignore
    @Test
    fun loading_spinner_should_be_shown_and_bottom_sheet_hidden_while_loading() {
        // given
        val uiSheetState = DetailUiState()

        // when
        composeTestRule.setContent {
            AppTheme {
                DetailContent(uiState = uiSheetState, handleEvent = {})
            }
        }

        // then
        composeTestRule.onNodeWithTag("LOADING_SPINNER").assertIsDisplayed()
        composeTestRule.onNodeWithTag("DETAIL_BOTTOM_SHEET").assertIsNotDisplayed()
    }

    @Ignore
    @Test
    fun detail_bottom_sheet_should_be_half_open_initially() {
        // given
        val uiSheetState = DetailUiState()

        // when
        composeTestRule.setContent {
            AppTheme {
                DetailContent(uiState = uiSheetState, handleEvent = {})
            }
        }

        // then
        composeTestRule.onNodeWithTag("DETAIL_BOTTOM_SHEET").assertIsDisplayed()
    }
}
