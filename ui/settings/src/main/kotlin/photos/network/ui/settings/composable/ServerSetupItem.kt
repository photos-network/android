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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import photos.network.api.ServerStatus
import photos.network.ui.settings.FormInput
import photos.network.ui.settings.R

@Composable
fun ServerSetupItem(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    serverHost: String = "",
    onServerHostUpdated: (String) -> Unit = {},
    isHostVerified: Boolean = false,
    clientId: String = "",
    onClientIdUpdated: (String) -> Unit = {},
    isClientIdVerified: Boolean = false,
    serverStatus: ServerStatus = ServerStatus.UNAVAILABLE,
    onServerSetupClicked: () -> Unit = {},
) {
    val serverSetupLabel = if (serverStatus != ServerStatus.AVAILABLE) {
        stringResource(id = R.string.settings_item_server_setup)
    } else {
        stringResource(id = R.string.settings_item_server_update)
    }
    Surface(
        modifier = modifier
            .clickable(
                onClickLabel = serverSetupLabel,
            ) {
                onServerSetupClicked()
            },
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = serverSetupLabel,
            )
            if (isExpanded) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                )
            } else {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                )
            }
        }
    }

    Column {
        // server host
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + expandVertically(),
            exit = shrinkVertically(animationSpec = tween(durationMillis = 500, delayMillis = 0)),
        ) {
            FormInput(
                modifier = modifier,
                label = "Host",
                value = serverHost,
                hint = "https://",
                onValueChanged = {
                    onServerHostUpdated(it)
                },
                showTrailingIcon = isHostVerified,
            )
        }

        // client id
        AnimatedVisibility(
            visible = isExpanded && isHostVerified,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + expandVertically(),
            exit = shrinkVertically(animationSpec = tween(durationMillis = 500, delayMillis = 0)),
        ) {
            FormInput(
                modifier = modifier,
                label = "Client ID",
                value = clientId,
                onValueChanged = {
                    onClientIdUpdated(it)
                },
                showTrailingIcon = isClientIdVerified,
            )
        }
    }
}
