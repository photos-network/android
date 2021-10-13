package network.photos.android.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import kotlinx.coroutines.flow.MutableStateFlow
import network.photos.android.MainActivity
import network.photos.android.app.onboarding.setup.SetupScreen
import network.photos.android.app.onboarding.setup.SetupUiState
import network.photos.android.theme.AppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
