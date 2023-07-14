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

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import photos.network.ui.common.R
import photos.network.ui.common.theme.AppTheme

/**
 * Tag used for categories etc.
 */
@Composable
fun Tag(
    tag: String,
    onClickTag: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10.dp),
            )
            .clip(RoundedCornerShape(5.dp))
            .clickable(onClick = { onClickTag(tag) })
            .padding(start = 2.dp, end = 4.dp),

    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(vertical = 4.dp),
            imageVector = Icons.Default.Bookmarks,
            contentDescription = stringResource(id = R.string.icon_tags),
            tint = MaterialTheme.colorScheme.secondary,
        )
        Text(
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
            text = tag,
            fontSize = MaterialTheme.typography.labelSmall.fontSize,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Preview(name = "Tag")
@Preview(name = "Tag Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TagPreview() {
    AppTheme {
        Tag(
            tag = "Landscape",
            onClickTag = {},
        )
    }
}
