package network.photos.android.composables

import androidx.activity.ComponentActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import kotlinx.coroutines.flow.MutableStateFlow
import network.photos.android.app.composables.AppTheme
import network.photos.android.common.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SetupScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val activity by lazy { composeTestRule.activity }

    private val buttonIsTriggered = MutableStateFlow(false)

    private val host: MutableState<String> = mutableStateOf("")
    private val clientId: MutableState<String> = mutableStateOf("")
    private val clientSecret: MutableState<String> = mutableStateOf("")
    private val isConnectionCheckInProgress: MutableState<Boolean> = mutableStateOf(false)
    private val isConnectionValid: MutableState<Boolean> = mutableStateOf(false)

    @Before
    fun setUp() {
        composeTestRule.setContent {
            AppTheme {
                SetupScreen(
                    host = host,
                    clientId = clientId,
                    clientSecret = clientSecret,
                    isConnectionCheckInProgress = isConnectionCheckInProgress,
                    isConnectionValid = isConnectionValid,
                    onButtonClick = {
                        buttonIsTriggered.value = true
                    },
                    onHelpClick = {}
                )
            }
        }
    }

    @Test
    fun nextButtonShouldTriggerCallback() {
        // given
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("TAG")

        findTextInputFieldForHost().performTextInput("http://127.0.0.1")
        findTextInputFieldForClientId().performTextInput("ABcdEFgh==")
        findTextInputFieldForClientSecret().performTextInput("JKlmNO==")

        // when
        findNextButton().performClick()

        // then
        assert(host.value == "http://127.0.0.1")
        assert(clientId.value == "ABcdEFgh==")
        assert(clientSecret.value == "JKlmNO==")
        assert(buttonIsTriggered.value)
    }

    private fun findTextInputFieldForHost(): SemanticsNodeInteraction {
        return composeTestRule.onNodeWithText(activity.getString(R.string.setup_host_label)).onParent()
    }

    private fun findTextInputFieldForClientId(): SemanticsNodeInteraction {
        return composeTestRule.onNodeWithText(activity.getString(R.string.setup_client_id_label)).onParent()
    }

    private fun findTextInputFieldForClientSecret(): SemanticsNodeInteraction {
        return composeTestRule.onNodeWithText(activity.getString(R.string.setup_client_secret_label)).onParent()
    }

    private fun findNextButton() =
        composeTestRule.onNodeWithText(activity.getString(R.string.button_next))
}
