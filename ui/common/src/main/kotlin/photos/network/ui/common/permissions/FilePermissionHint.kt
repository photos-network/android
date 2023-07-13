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
package photos.network.ui.common.permissions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FilePermissionHint(
    onHintClicked: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(modifier = Modifier.padding(4.dp).fillMaxWidth()) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Permission required",
            )
            IconButton(onClick = onCloseClicked) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "To show images stored on this device, the permission to read external storage is mandatory.",
        )
        Button(onClick = onHintClicked) {
            Text("Grant access")
        }
        Divider(modifier = Modifier.height(1.dp))
    }
}
//
//            if (permissionStateLocation.status is PermissionStatus.Denied) {
//                // TODO: show hint for permission request
//            }

@Preview
@Composable
private fun FilePermissionHintPreview() {
    MaterialTheme {
        FilePermissionHint(
            onCloseClicked = {},
            onHintClicked = {},
        )
    }
}
