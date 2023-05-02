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
package photos.network.ui.photos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil.compose.rememberImagePainter
import photos.network.repository.photos.Photo
import photos.network.ui.PhotoBottomIcons
import photos.network.ui.PhotoTopIcons

@Composable
fun PhotoDetails(
    modifier: Modifier = Modifier,
    selectedIndex: Int?,
    selectedPhoto: Photo,
    selectNextPhoto: () -> Unit,
    selectPreviousPhoto: () -> Unit,
    onSelectItem: (index: Int?) -> Unit,
) {
    val data = if (selectedPhoto.uri != null) {
        selectedPhoto.uri
    } else {
        selectedPhoto.imageUrl
    }

    val swipeableState = rememberSwipeableState(
        initialValue = 0,
        confirmStateChange = { newValue: Int ->
            selectedIndex.let {
                if (newValue == 0) {
                    selectNextPhoto()
                } else {
                    selectPreviousPhoto()
                }
                true
            }
            false
        },
    )

    Box(modifier = modifier) {
        Image(
            modifier = Modifier
                .swipeable(
                    state = swipeableState,
                    anchors = mapOf(0f to 0, 1f to 1),
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal,
                )
                .fillMaxSize(),
            painter = rememberImagePainter(
                data = data,
                builder = {
                    crossfade(true)
                    placeholder(R.drawable.image_placeholder)
                },
            ),
            contentDescription = null,
        )

        PhotoTopIcons(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .align(Alignment.TopStart),
            onBackPressed = { onSelectItem(null) },
        )

        PhotoBottomIcons(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .align(Alignment.BottomCenter),
        )
    }
}
