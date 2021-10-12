package network.photos.android.composables

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import kotlinx.coroutines.flow.MutableStateFlow
import network.photos.android.R
import network.photos.android.app.onboarding.setup.SetupScreen
import network.photos.android.theme.AppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SetupScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val activity by lazy { composeTestRule.activity }

    private val buttonIsTriggered = MutableStateFlow(false)

    private val host: String = ""
    private val clientId: String = ""
    private val clientSecret: String = ""
    private val isConnectionCheckInProgress: Boolean = false
    private val isConnectionValid: Boolean = false

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
                    onNextClick = {
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
        assert(host == "http://127.0.0.1")
        assert(clientId == "ABcdEFgh==")
        assert(clientSecret == "JKlmNO==")
        assert(buttonIsTriggered.value)
    }

    private fun findTextInputFieldForHost(): SemanticsNodeInteraction {
        return composeTestRule.onNodeWithText(activity.getString(R.string.setup_host_label))
            .onParent()
    }

    private fun findTextInputFieldForClientId(): SemanticsNodeInteraction {
        return composeTestRule.onNodeWithText(activity.getString(R.string.setup_client_id_label))
            .onParent()
    }

    private fun findTextInputFieldForClientSecret(): SemanticsNodeInteraction {
        return composeTestRule.onNodeWithText(activity.getString(R.string.setup_client_secret_label))
            .onParent()
    }

    private fun findNextButton() =
        composeTestRule.onNodeWithText(activity.getString(R.string.button_next))
}
