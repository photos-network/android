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
package photos.network.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.getViewModel
import photos.network.R
import photos.network.theme.AppTheme
import photos.network.ui.TextInput
import photos.network.ui.components.AppLogo

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val viewmodel: SettingsViewModel = getViewModel()

    AccountContent(
        modifier = modifier,
        navController = navController,
        uiState = viewmodel.uiState.value,
        handleEvent = viewmodel::handleEvent,
    )
}

@Composable
fun AccountContent(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    uiState: SettingsUiState,
    handleEvent: (event: SettingsEvent) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        // header + icon
        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // header gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x55000000),
                                Color(0x00000000)
                            )
                        )
                    )
            )

            // app name
            Text(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.app_name_full),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            // logo with status indicator
            AppLogo(
                modifier = Modifier
                    .padding(top = 125.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center),
                size = 150.dp,
                serverStatus = uiState.serverStatus,
            )
        }

        // buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        // TODO: handle click
                    }
            ) {

                // Sync
                IconButton(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        .align(Alignment.CenterHorizontally),
                    enabled = false,
                    onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Sync,
                        contentDescription = null
                    )
                }
                Text(
                    text = "Force Sync",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        // TODO: handle click
                    }
            ) {
                IconButton(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        .align(Alignment.CenterHorizontally),
                    onClick = {}) {
                    Icon(Icons.Filled.Person, null)
                }
                Text(
                    text = "Edit Profile",
                    color = MaterialTheme.colorScheme.onBackground

                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        // TODO: handle click
                    }
            ) {
                IconButton(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        .align(Alignment.CenterHorizontally),
                    onClick = {},
                    enabled = false
                ) {
                    Icon(
                        imageVector = Icons.Filled.List,
                        contentDescription = null,
                    )
                }
                Text(
                    text = "Activity Log",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Divider(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 8.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.outline
        )

        Column(
            modifier = Modifier.padding(16.dp),
        ) {

            // status
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Add a photos.network instance configuration to sync your data with.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )

            TextInput(
                modifier = Modifier.padding(8.dp),
                label = "Host",
                value = "https://",
                onValueChanged = {}
            )

            TextInput(
                modifier = Modifier.padding(8.dp),
                label = "Client ID",
                value = "",
                onValueChanged = {}
            )

            TextInput(
                modifier = Modifier.padding(8.dp),
                label = "Client Secret",
                value = "",
                onValueChanged = {}
            )
        }
    }

    // TODO: bottom sheet for server connection settings
}

@Preview(
    "Account",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    "Account • Dark",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewAccount(
    @PreviewParameter(PreviewAccountProvider::class) uiState: SettingsUiState,
) {
    AppTheme {
        AccountContent(
            uiState = uiState,
            handleEvent = {}
        )
    }
}

internal class PreviewAccountProvider : PreviewParameterProvider<SettingsUiState> {
    override val values = sequenceOf(
        SettingsUiState(serverStatus = ServerStatus.PROGRESS),
        SettingsUiState(serverStatus = ServerStatus.UNAVAILABLE),
        SettingsUiState(serverStatus = ServerStatus.AVAILABLE),
    )
    override val count: Int = values.count()
}
