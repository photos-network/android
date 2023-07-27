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
package photos.network.ui.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import photos.network.ui.common.ReferenceDevices
import photos.network.ui.common.theme.AppTheme

@Composable
fun FormInput(
    modifier: Modifier = Modifier,
    label: String = "",
    value: String = "",
    onValueChanged: (String) -> Unit = {},
    hint: String = "",
    trailingIcon: ImageVector? = null,
) {
    Surface(modifier = modifier) {
        var text by remember { mutableStateOf(value) }

        TextField(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 8.dp)
                .fillMaxWidth(),
            value = text,
            onValueChange = {
                text = it
                onValueChanged(it)
            },
            enabled = true,
            readOnly = false,
            textStyle = MaterialTheme.typography.bodyMedium,
            label = {
                Text(label)
            },
            placeholder = {
                Text(text = hint)
            },
            leadingIcon = null,
            trailingIcon = {
                trailingIcon?.let {
                    Icon(
                        imageVector = trailingIcon,
                        tint = Color(0xFF4CAF50),
                        contentDescription = null,
                    )
                }
            },
            isError = false,
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onBackground,
            ),
        )
    }
}

@ReferenceDevices
@Composable
private fun FormInput() {
    AppTheme {
        FormInput(
            label = "Label",
            value = "Content",
            trailingIcon = Icons.Default.Check,
        )
    }
}
