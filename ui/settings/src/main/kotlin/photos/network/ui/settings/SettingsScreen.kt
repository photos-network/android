/*
 * Copyright 2020-2023 Photos.network developers
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
package photos.network.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.getViewModel
import photos.network.api.ServerStatus
import photos.network.ui.common.ReferenceDevices
import photos.network.ui.common.navigation.Destination
import photos.network.ui.common.theme.AppTheme
import photos.network.ui.settings.composable.AccountSetupItem
import photos.network.ui.settings.composable.AppVersionItem
import photos.network.ui.settings.composable.ServerSetupItem
import photos.network.ui.settings.composable.SettingsHeader

/**
 * stateful
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val viewmodel: SettingsViewModel = getViewModel()

    SettingsScreen(
        modifier = modifier,
        uiState = viewmodel.uiState.collectAsState().value,
        handleEvent = viewmodel::handleEvent,
        navigateToLogin = {
            navController.navigate(
                "${Destination.Login.route}/${viewmodel.uiState.value.host}/${viewmodel.uiState.value.clientId}",
            )
        },
    )
}

/**
 * stateless
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    handleEvent: (event: SettingsEvent) -> Unit = {},
    navigateToLogin: () -> Unit = {},
) {
    val verticalScrollState = rememberScrollState(0)

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .verticalScroll(verticalScrollState),
    ) {
        SettingsHeader(serverStatus = uiState.serverStatus)

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(id = R.string.settings_features_pre),
            color = MaterialTheme.colorScheme.onBackground,
        )
        val map = listOf(
            stringResource(id = R.string.feature_sharing_title) to stringResource(id = R.string.feature_sharing_description),
            stringResource(id = R.string.feature_backup_title) to stringResource(id = R.string.feature_backup_description),
            stringResource(id = R.string.feature_image_analysis_title) to stringResource(id = R.string.feature_image_analysis_description),
        )

        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            map.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .size(8.dp)
                            .background(MaterialTheme.colorScheme.onBackground, shape = CircleShape),
                    )

                    Text(
                        text = it.first,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 24.dp),
                    text = it.second,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            text = stringResource(id = R.string.settings_features_post),
            color = MaterialTheme.colorScheme.onBackground,
        )

        Divider()

        ServerSetupItem(
            onServerSetupClicked = {
                handleEvent(SettingsEvent.ToggleServerSetup)
            },
            isExpanded = uiState.isServerSetupExpanded,
            serverHost = uiState.host,
            onServerHostUpdated = {
                handleEvent(SettingsEvent.HostChanged(it))
            },
            isHostVerified = uiState.isHostVerified,
            clientId = uiState.clientId,
            onClientIdUpdated = {
                handleEvent(SettingsEvent.ClientIdChanged(it))
            },
            isClientIdVerified = uiState.isClientVerified,
        )

        AnimatedVisibility(visible = uiState.isClientVerified) {
            Divider()

            AccountSetupItem(loggedIn = uiState.loggedIn) {
                navigateToLogin()
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        AppVersionItem(version = uiState.appVersion) {
            handleEvent(SettingsEvent.SetClipboardEvent)
        }
    }
}

@ReferenceDevices
@Composable
private fun Settings(
    @PreviewParameter(PreviewAccountProvider::class) uiState: SettingsUiState,
) {
    AppTheme {
        SettingsScreen(
            uiState = uiState,
            handleEvent = {},
        )
    }
}

internal class PreviewAccountProvider : PreviewParameterProvider<SettingsUiState> {
    override val values = sequenceOf(
        SettingsUiState(
            serverStatus = ServerStatus.UNAVAILABLE,
            isServerSetupExpanded = true,
            isHostVerified = true,
        ),
        SettingsUiState(serverStatus = ServerStatus.PROGRESS, isServerSetupExpanded = true),
        SettingsUiState(serverStatus = ServerStatus.AVAILABLE, isServerSetupExpanded = true),
    )
    override val count: Int = values.count()
}
