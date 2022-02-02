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

import android.icu.text.DateFormatSymbols
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.Instant
import java.time.ZoneOffset
import logcat.logcat
import org.koin.androidx.compose.viewModel
import photos.network.R
import photos.network.data.photos.entities.Photo
import photos.network.home.HomeViewModel
import photos.network.navigation.Destination

@Composable
fun PhotosScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberAnimatedNavController(),
) {
    val viewModel: HomeViewModel by viewModel()

    val uiState by viewModel.uiState.collectAsState()

    PhotosScreen(
        modifier = modifier,
        navController = navController,
        isLoading = uiState.loading,
        photos = uiState.photos,
        onRefreshPhotos = {
            viewModel.refreshPhotos()
        },
    )
}

@Composable
fun PhotosScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberAnimatedNavController(),
    isLoading: Boolean = false,
    photos: List<Photo> = emptyList(),
    onRefreshPhotos: () -> Unit = {},
) {
    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(isLoading),
        onRefresh = { onRefreshPhotos() },
    ) {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(90.dp),
            modifier
                .fillMaxSize()
                .padding(4.dp),
        ) {
            // group by month
            val groupedPhotos = photos.groupBy {
                it.dateTaken.atZone(ZoneOffset.UTC).month
            }

            groupedPhotos.forEach { (month, photos) ->
                // add year if not matching with current year
                val yearOfFirst = photos[0].dateTaken.atZone(ZoneOffset.UTC).year
                val yearNow = Instant.now().atZone(ZoneOffset.UTC).year
                val title = if (yearOfFirst == yearNow) {
                    DateFormatSymbols().months[month.value - 1]
                } else {
                    "${DateFormatSymbols().months[month.value - 1]} $yearOfFirst"
                }

                // TODO: add header instead of single item
                item {
                    Text(text = title)
                }

                items(photos.size) { index: Int ->
                    logcat { "path: ${photos[index].imageUrl}, date: ${photos[index].dateTaken}" }
                    Image(
                        painter = rememberImagePainter(
                            data = photos[index].imageUrl,
                            builder = {
                                crossfade(true)
                                placeholder(R.drawable.image_placeholder)
                            }
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clickable {
                                navController.navigate("${Destination.Details.route}/${photos[index].id}")
                            }
                            .clip(RoundedCornerShape(2.dp))
                            .size(128.dp),
                    )
                }
            }
        }
    }
}
