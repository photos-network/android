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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import photos.network.ui.common.BooleanProvider
import photos.network.ui.common.ReferenceDevices
import photos.network.ui.common.theme.AppTheme
import photos.network.ui.settings.R

@Composable
fun AccountSetupItem(
    modifier: Modifier = Modifier,
    loggedIn: Boolean = false,
    onAccountSetupClicked: () -> Unit = {},
) {
    val clickLabel: String = if (loggedIn) {
        stringResource(id = R.string.settings_account_logout)
    } else {
        stringResource(id = R.string.settings_account_login)
    }
    Surface(
        modifier = modifier
            .clickable(
                onClickLabel = clickLabel,
            ) {
                onAccountSetupClicked()
            },
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = clickLabel,
            )
        }
    }
}

@ReferenceDevices
@Composable
private fun AccountSetupItem(
    @PreviewParameter(BooleanProvider::class) loggedIn: Boolean,
) {
    AppTheme {
        AccountSetupItem(
            modifier = Modifier,
            loggedIn = loggedIn,
        )
    }
}
