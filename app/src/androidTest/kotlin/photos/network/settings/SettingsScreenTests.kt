package photos.network.settings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class SettingsScreenTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun title_should_be_displayed_in_settings_screen() {
        // given
        composeTestRule.setContent {
            SettingsScreen()
        }

        // then
        composeTestRule.onNodeWithTag("SETTINGS_HEADER_TITLE").assertIsDisplayed()
    }

    @Test
    fun logo_should_be_displayed_in_settings_screen() {
        // given
        composeTestRule.setContent {
            SettingsScreen()
        }

        // then
        composeTestRule.onNodeWithTag("SETTINGS_HEADER_LOGO").assertIsDisplayed()
    }
}
