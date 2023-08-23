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

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import photos.network.ui.common.theme.AppTheme

@Composable
fun PhotoTopIcons(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
) {
    Row(modifier = modifier) {
        IconButton(
            onClick = { onBackPressed() },
        ) {
            Icon(Icons.Filled.ArrowBack, null, tint = Color.LightGray)
        }
    }
}

@Preview
@Composable
private fun PhotoTopIconPreview() {
    AppTheme {
        PhotoTopIcons()
    }
}
