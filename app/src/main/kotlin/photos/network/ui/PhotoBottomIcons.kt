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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import photos.network.data.photos.repository.Photo
import photos.network.theme.AppTheme

@Composable
fun PhotoBottomIcons(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(
            onClick = {
                // TODO: show share dialog
            }
        ) {
            Icon(Icons.Default.Share, null, tint = Color.White)
        }

        IconButton(
            onClick = {
                // TODO: show photo size selection dialog
            }
        ) {
            Icon(Icons.Default.Download, null, tint = Color.White)
        }

        IconButton(
            onClick = {
                // TODO: change photo privacy settings
            }
        ) {
            // TODO: change icon regarding photo settings
            if (true) {
                Icon(Icons.Default.Shield, null, tint = Color.White)
            } else {
                Icon(Icons.Outlined.Shield, null, tint = Color.White)
            }
        }

        IconButton(
            onClick = {
                // TODO: show delete dialog
            }
        ) {
            Icon(Icons.Default.Delete, null, tint = Color.White)
        }
    }
}

@Preview
@Composable
private fun PhotoBottomIconsPreview() {
    AppTheme {
        PhotoBottomIcons()
    }
}
