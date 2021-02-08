package network.photos.android.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import network.photos.android.app.composables.AppTheme
import network.photos.android.app.composables.AppTypography
import network.photos.android.common.R

@Preview(name = "Setup")
@Composable
fun PreviewSetupScreen() {
    val host: MutableState<String> = mutableStateOf("")
    val clientId: MutableState<String> = mutableStateOf("")
    val clientSecret: MutableState<String> = mutableStateOf("")
    val isConnectionCheckInProgress: MutableState<Boolean> = mutableStateOf(false)
    val isConnectionValid: MutableState<Boolean> = mutableStateOf(false)
    SetupScreen(
        host = host,
        clientId = clientId,
        clientSecret = clientSecret,
        isConnectionCheckInProgress = isConnectionCheckInProgress,
        isConnectionValid = isConnectionValid,
        onButtonClick = {},
        onHelpClick = {}
    )
}

@Composable
fun SetupScreen(
    host: MutableState<String>,
    clientId: MutableState<String>,
    clientSecret: MutableState<String>,
    isConnectionCheckInProgress: MutableState<Boolean>,
    isConnectionValid: MutableState<Boolean>,
    onButtonClick: () -> Unit,
    onHelpClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val padding = 24.dp

        Text(
            text = "Welcome",
            style = AppTypography.h4
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center,
            text = "Please enter the connection details of your photos.network instance.",
            style = AppTypography.body1
        )

        Spacer(Modifier.preferredSize(padding))

        TextInput(
            label = stringResource(id = R.string.setup_host_label),
            name = host.value,
            enabled = !isConnectionCheckInProgress.value,
            onNameChange = {
                host.value = it
            }
        )

        TextInput(
            label = stringResource(id = R.string.setup_client_id_label),
            name = clientId.value,
            enabled = !isConnectionCheckInProgress.value,
            onNameChange = {
                clientId.value = it
            })

        TextInput(
            label = stringResource(id = R.string.setup_client_secret_label),
            name = clientSecret.value,
            enabled = !isConnectionCheckInProgress.value,
            onNameChange = {
                clientSecret.value = it
            })

        Spacer(Modifier.preferredSize(padding))

        Button(
            onClick = onButtonClick,
            modifier = Modifier.layoutId("button"),
            enabled = !isConnectionCheckInProgress.value
        ) {
            Text(text = stringResource(id = R.string.button_next))
        }

        if (isConnectionCheckInProgress.value) {
            Spacer(Modifier.preferredSize(4.dp))
            LinearProgressIndicator()
        }

        Spacer(Modifier.preferredSize(padding))

        Text(
            modifier = Modifier.clickable(onClick = onHelpClick),
            text = "Where to find these informations?",
            style = AppTypography.subtitle1
        )
    }
}
