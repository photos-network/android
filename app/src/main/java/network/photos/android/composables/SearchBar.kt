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
package network.photos.android.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import network.photos.android.R
import network.photos.android.theme.AppTheme

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    search: String = "",
    searchTextChanged: (String) -> Unit = {},
    onSearch: (String) -> Unit = {}
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(56.dp),
        value = search,
        onValueChange = { searchTextChanged(it) },
        label = { Text(text = stringResource(id = R.string.search_hint)) },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch(search)
        })
    )
}

@Preview(
    name = "Night",
    showSystemUi = false,
    locale = "ar",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4
)
@Preview(name = "Day", locale = "ko-rKR", showSystemUi = false, device = Devices.PIXEL_4)
@Preview(name = "Day, small screen", showSystemUi = true, device = Devices.NEXUS_5)
@Composable
private fun Preview() {
    var search by remember { mutableStateOf("") }
    AppTheme {
        SearchBar(
            search = search,
            searchTextChanged = { search = it }
        ) {
            // search for string
        }
    }
}
