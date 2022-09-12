/*
 * Copyright 2020-2022 Photos.network developers
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
package photos.network.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import photos.network.theme.AppTheme

@Composable
fun DecoratedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    label: String? = null,
    hint: String? = null,
    error: String? = null,
    onFocusChanged: (FocusState) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = PasswordVisualTransformation(),
//    visualTransformation: VisualTransformation = VisualTransformation.None,
    onKeyActionNext: () -> Unit = {},
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading icon
        leadingIcon?.let { it() }

        // content
        Column(modifier = Modifier.weight(1f)) {
            // Label
//            label?.let {
//                Text(
//                    text = label,
//                    style = MaterialTheme.typography.labelSmall,
//                    color = MaterialTheme.colorScheme.outline,
//                )
//            }

            Box {
                // Value
                TextField(
                    value = value,
                    onValueChange = {
                        onValueChanged(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = true,
                    readOnly = false,
                    textStyle = MaterialTheme.typography.bodyMedium,
//                    label = label,
//                    placeholder = hint,
                    leadingIcon = null,
                    trailingIcon = null,
                    isError = false,
//                    visualTransformation = visualTransformation,
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Password,
//                        imeAction = ImeAction.Next
//                    ),
//                    keyboardActions = KeyboardActions(
//                        onNext = { onKeyActionNext() }
//                    ),
                    singleLine = true,
                    maxLines = 1,
                )

                // Hint
                if (hint != null && value.isEmpty()) {
                    Text(
                        text = hint,
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            // Error
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }

        // Trailing icon
        trailingIcon?.let { it() }
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewCustomDecoratedTextField() {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    AppTheme {
        Column {

            DecoratedTextField(
                value = user,
                label = "Username",
                hint = "user",
                error = if (user.length > 3 && user != "foo") {
                    "invalid"
                } else {
                    null
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        tint = MaterialTheme.colorScheme.outline,
                        contentDescription = null
                    )
                },
                onValueChanged = {
                    user = it
                },
                onKeyActionNext = {},
                onFocusChanged = {},
                visualTransformation = VisualTransformation.None,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Check,
                        tint = MaterialTheme.colorScheme.outline,
                        contentDescription = null
                    )
                }
            )

            DecoratedTextField(
                value = pass,
                label = "Password",
                hint = "pass",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        tint = MaterialTheme.colorScheme.outline,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            pass = ""
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                    )
                },
                onValueChanged = {
                    pass = it
                },
                onKeyActionNext = {},
                onFocusChanged = {},
            )
        }
    }
}
