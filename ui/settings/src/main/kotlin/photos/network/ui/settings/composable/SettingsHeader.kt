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
package photos.network.ui.settings.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import photos.network.api.ServerStatus
import photos.network.ui.common.ReferenceDevices
import photos.network.ui.common.components.AppLogo
import photos.network.ui.common.theme.AppTheme
import photos.network.ui.settings.R

@Suppress("MagicNumber")
@Composable
internal fun SettingsHeader(
    modifier: Modifier = Modifier,
    serverStatus: ServerStatus,
) {
    // header + icon
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
    ) {
        // header gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(MaterialTheme.colorScheme.primary)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x55000000),
                            Color(0x00000000),
                        ),
                    ),
                ),
        )

        // app name
        Text(
            modifier = Modifier
                .padding(top = 24.dp)
                .testTag("SETTINGS_HEADER_TITLE")
                .fillMaxWidth(),
            text = stringResource(id = R.string.app_name_full),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = Color.White,
        )

        // logo with status indicator
        AppLogo(
            modifier = Modifier
                .padding(top = 90.dp)
                .testTag("SETTINGS_HEADER_LOGO")
                .fillMaxWidth()
                .align(Alignment.Center),
            size = 150.dp,
            serverStatus = serverStatus,
        )
    }
}

@ReferenceDevices
@Composable
private fun Header(
    @PreviewParameter(ServerStatusProvider::class) status: ServerStatus,
) {
    AppTheme {
        SettingsHeader(serverStatus = status)
    }
}
internal class ServerStatusProvider : PreviewParameterProvider<ServerStatus> {
    override val values = sequenceOf(
        ServerStatus.AVAILABLE,
        ServerStatus.PROGRESS,
        ServerStatus.UNAVAILABLE,
        ServerStatus.UNAUTHORIZED,
    )
    override val count: Int = values.count()
}
