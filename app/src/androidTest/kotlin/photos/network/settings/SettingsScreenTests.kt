/*
 * Copyright 2020-2022 Photos.network developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
