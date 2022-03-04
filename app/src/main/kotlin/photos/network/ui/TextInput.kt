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
package photos.network.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import photos.network.theme.AppTheme

@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    label: String?,
    value: String, /* state */
    enabled: Boolean = true,
    onValueChanged: (String) -> Unit /* event */,
    labelColor: Color = MaterialTheme.colorScheme.secondary,
    valueColor: Color = MaterialTheme.colorScheme.secondary,
    errroColor: Color = MaterialTheme.colorScheme.error,
    isValid: () -> (Boolean) = { true }
) {
    Column(
        modifier = modifier
    ) {
        label?.let {
            Text(
                modifier = Modifier.padding(all = 0.dp),
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = labelColor
            )
        }
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    BorderStroke(
                        1.dp,
                        if (isValid()) valueColor else errroColor
                    )
                )
                .padding(horizontal = 8.dp),
            value = value,
            maxLines = 2,
            textStyle = TextStyle(
                color = valueColor,
                fontWeight = FontWeight.Medium
            ),
            onValueChange = onValueChanged
        )
    }
}

@Preview
@Composable
fun PreviewTextInput() {
    AppTheme {
        Column(modifier = Modifier.padding(8.dp)) {
            TextInput(
                label = "Label",
                value = "content",
                onValueChanged = {}
            )
        }
    }
}
