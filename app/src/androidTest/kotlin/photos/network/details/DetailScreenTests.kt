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
