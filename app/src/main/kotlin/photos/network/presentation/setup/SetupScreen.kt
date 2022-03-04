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
package photos.network.presentation.setup

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.viewModel
import photos.network.R
import photos.network.navigation.Destination
import photos.network.ui.TextInput
import photos.network.theme.AppTheme

/**
 * Initial app screen to setup photos network instance and client secrets used for
 * the communicating with the core backend.
 */
@Composable
fun SetupScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    val viewModel: SetupViewModel by viewModel()

    val uiState by viewModel.uiState.collectAsState()

    SetupScreen(
        modifier = modifier,
        host = uiState.host,
        clientId = uiState.clientId,
        clientSecret = uiState.clientSecret,
        isConnectionCheckInProgress = uiState.isConnectionCheckInProgress,
        isConnectionValid = uiState.isConnectionValid,
        onHostChanged = { host -> viewModel.setHost(host) },
        onClientIdChanged = { viewModel.setClientId(it) },
        onClientSecretChanged = { viewModel.setClientSecret(it) },
        onNextClick = {
            viewModel.checkConnection {
                navController.navigate(Destination.Login.route) {
                    launchSingleTop = true
                    popUpTo(Destination.Login.route) {
                        inclusive = true
                    }
                }
            }
        },
        onHelpClick = {
            navController.navigate(Destination.Help.route)
        },
    )
}

@Composable
fun SetupScreen(
    modifier: Modifier = Modifier,
    host: String? = null,
    clientId: String? = null,
    clientSecret: String? = null,
    isConnectionCheckInProgress: Boolean = false,
    isConnectionValid: Boolean = false,
    onHostChanged: (String) -> Unit = {},
    onClientIdChanged: (String) -> Unit = {},
    onClientSecretChanged: (String) -> Unit = {},
    onNextClick: () -> Unit = {},
    onHelpClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val padding = 24.dp

        Text(
            text = "Welcome",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center,
            text = "Please enter the connection details of your photos.network instance.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.size(padding))

        TextInput(
            modifier = Modifier.testTag("host").fillMaxWidth(),
            enabled = !isConnectionCheckInProgress,
            label = stringResource(id = R.string.setup_host_label),
            value = host ?: "",
            onValueChanged = {
                onHostChanged(it)
            }
        )

        TextInput(
            modifier = Modifier.testTag("clientID").fillMaxWidth(),
            label = stringResource(id = R.string.setup_client_id_label),
            value = clientId ?: "",
            enabled = !isConnectionCheckInProgress,
            onValueChanged = { newClientId ->
                onClientIdChanged(newClientId)
            }
        )

        TextInput(
            modifier = Modifier.testTag("clientSecret").fillMaxWidth(),
            label = stringResource(id = R.string.setup_client_secret_label),
            value = clientSecret ?: "",
            enabled = !isConnectionCheckInProgress,
            onValueChanged = { newClientSecret ->
                onClientSecretChanged(newClientSecret)
            }
        )

        Spacer(Modifier.size(padding))

        Button(
            onClick = onNextClick,
            modifier = Modifier.testTag("buttonNext").layoutId("button"),
            enabled = !isConnectionCheckInProgress
        ) {
            Text(text = stringResource(id = R.string.button_next))
        }

        if (isConnectionCheckInProgress) {
            Spacer(Modifier.size(4.dp))
            LinearProgressIndicator()
        }

        Spacer(Modifier.size(padding))

        Text(
            modifier = Modifier.clickable(onClick = onHelpClick),
            text = "Where to find these informations?",
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview(name = "Setup")
@Preview(name = "Setup · DARK", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSetupScreen() {
    AppTheme {
        SetupScreen()
    }
}

@Preview(name = "Setup » Loading")
@Preview(name = "Setup » Loading · DARK", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSetupScreenLoading() {
    AppTheme {
        var host by rememberSaveable { mutableStateOf("") }

        SetupScreen(
            host = host,
            onHostChanged = {
                host = it
            },
            isConnectionCheckInProgress = true
        )
    }
}
