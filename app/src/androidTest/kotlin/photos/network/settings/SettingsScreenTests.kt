package photos.network.settings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import photos.network.R

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
