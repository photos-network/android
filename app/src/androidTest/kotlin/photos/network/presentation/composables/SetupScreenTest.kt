/*
 * Copyright 2020-2021 Photos.network developers
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
package photos.network.presentation.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import photos.network.MainActivity
import photos.network.presentation.setup.SetupScreen
import photos.network.presentation.setup.SetupUiState
import photos.network.theme.AppTheme

class SetupScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val buttonIsTriggered = MutableStateFlow(false)
    private var uiState by mutableStateOf(SetupUiState(loading = true))

    @Before
    fun setUp() {
        composeTestRule.setContent {
            AppTheme {
                SetupScreen(
                    modifier = Modifier,
                    host = uiState.host,
                    clientId = uiState.clientId,
                    clientSecret = uiState.clientSecret,
                    isConnectionCheckInProgress = uiState.isConnectionCheckInProgress,
                    isConnectionValid = uiState.isConnectionValid,
                    onHostChanged = {
                        uiState = uiState.copy(host = it)
                    },
                    onClientIdChanged = {
                        uiState = uiState.copy(clientId = it)
                    },
                    onClientSecretChanged = {
                        uiState = uiState.copy(clientSecret = it)
                    },
                    onNextClick = {
                        buttonIsTriggered.value = true
                    },
                    onHelpClick = {},
                )
            }
        }
    }

    @Test
    fun nextButtonShouldTriggerCallback() {
        // given
        composeTestRule.onNodeWithTag("host").performTextInput("http://127.0.0.1")
        composeTestRule.onNodeWithTag("clientID").performTextInput("ABcdEFgh==")
        composeTestRule.onNodeWithTag("clientSecret").performTextInput("JKlmNO==")

        // when
        composeTestRule.onNodeWithTag("buttonNext").performClick()

        // then
        assert(uiState.host == "http://127.0.0.1")
        assert(uiState.clientId == "ABcdEFgh==")
        assert(uiState.clientSecret == "JKlmNO==")
        assert(buttonIsTriggered.value)
    }
}
