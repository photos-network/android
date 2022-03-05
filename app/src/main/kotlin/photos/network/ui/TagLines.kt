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
package photos.network.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import photos.network.theme.AppTheme

@Preview(name = "Tags")
@Preview(name = "Tags · DARK", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TagLinePreview() {
    AppTheme {
        TagLines(
            tags = listOf("Landscape", "Architectur"),
            onClickTag = {}
        )
    }
}

@Composable
fun TagLines(
    tags: List<String>,
    onClickTag: (String) -> Unit = {}
) {
    LazyRow {
        items(tags) { tag ->
            Tag(tag, onClickTag)
            Spacer(androidx.compose.ui.Modifier.size(4.dp))
        }
    }
}
