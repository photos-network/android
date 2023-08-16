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
package photos.network.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.accompanist.insets.LocalWindowInsets

class SearchContentProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf("Searchphrase", "")
    override val count: Int = values.count()
}

@Preview(
    name = "Night",
    showSystemUi = false,
    locale = "ar",
    uiMode = UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4,
)
@Preview(name = "Day", locale = "ko-rKR", showSystemUi = false, device = Devices.PIXEL_4)
@Preview(name = "Day, small screen", showSystemUi = true, device = Devices.NEXUS_5)
@Preview(name = "Search", showBackground = true)
@Preview(name = "Search · Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SearchViewPreview(@PreviewParameter(SearchContentProvider::class) search: String) {
    MaterialTheme {
        val textState = remember { mutableStateOf(TextFieldValue(search)) }
        Searchbar(
            value = textState.value,
            onValueChange = { newTextFieldValue -> textState.value = newTextFieldValue },
            onSearch = {},
            hint = stringResource(id = photos.network.ui.common.R.string.search_hint),
        )
    }
}

@Composable
fun Searchbar(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSearch: () -> Unit = {},
    hint: String = stringResource(id = photos.network.ui.common.R.string.search_hint),
    maxLength: Int = 200,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Search,
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Sentences,
    ),
    keyboardActions: KeyboardActions = KeyboardActions(onSearch = { onSearch() }),
) {
    Box(
        modifier = modifier.testTag("Searchbar"),
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        val hasWindowFocus = LocalWindowInfo.current.isWindowFocused
        val keyboardVisible = LocalWindowInsets.current.ime.isVisible

        var focused by remember { mutableStateOf(false) }

        val triggerSearch = {
            onSearch()
            keyboardController?.hide()
            focusManager.clearFocus()
        }

//        TextInput(
//            name = value,
//            onValueChanged = { value ->
//                if (value.text.length <= maxLength) onValueChange(value)
//            },
//            modifier = Modifier
//                .testTag("SearchbarInput")
//                .fillMaxWidth()
//                .padding(4.dp),
//            textStyle = TextStyle(color = MaterialTheme.colors.primary, fontSize = 18.sp),
//            placeholder = { Text(text = hint) },
//            leadingIcon = {
//                Icon(
//                    Icons.Default.Search,
//                    contentDescription = "",
//                    modifier = Modifier
//                        .padding(15.dp)
//                        .size(24.dp)
//                )
//            },
//            trailingIcon = {
//                AnimatedVisibility(
//                    visible = value.text.isNotEmpty(),
//                    enter = expandIn(expandFrom = Alignment.CenterStart),
//                    exit = shrinkOut(shrinkTowards = Alignment.CenterStart)
//                ) {
//                    UserAvatar(
//                        modifier = Modifier
//                            .size(32.dp),
//                        user = null)
//                }
//            },
//            keyboardOptions = keyboardOptions,
//            keyboardActions = keyboardActions,
//            singleLine = true,
//            maxLines = 1,
//            shape = RoundedCornerShape(5.dp),
//            colors = TextFieldDefaults.textFieldColors(
//                textColor = MaterialTheme.colors.primary,
//                cursorColor = MaterialTheme.colors.primary,
//                leadingIconColor = MaterialTheme.colors.primary,
//                trailingIconColor = MaterialTheme.colors.primary,
//                backgroundColor = MaterialTheme.colors.surface,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent
//            )
//        )
    }
}
