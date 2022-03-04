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
import androidx.compose.foundation.lazy.GridItemSpan
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import java.time.Instant
import java.time.ZoneOffset
import logcat.LogPriority
import logcat.logcat
import photos.network.R
import photos.network.data.photos.repository.Photo
import photos.network.theme.AppTheme

@Composable
fun PhotoGrid(
    modifier: Modifier = Modifier,
    photos: List<Photo>,
    onSelectItem: (id: String) -> Unit,
) {
    val lazyListState = rememberLazyListState()

    LazyVerticalGrid(
        state = lazyListState,
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        cells = GridCells.Adaptive(90.dp),
    ) {
        // group by year
        val groupedByYear = photos.groupBy {
            it.dateTaken.atZone(ZoneOffset.UTC).year
        }

        groupedByYear.forEach { (_, photos) ->
            val yearOfFirst = photos[0].dateTaken.atZone(ZoneOffset.UTC).year
            val yearNow = Instant.now().atZone(ZoneOffset.UTC).year

            // add year header if necessary
            if (yearOfFirst != yearNow) {
                item(span = { GridItemSpan(4) }) {
                    Text(
                        text = yearOfFirst.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // group by month
            val groupedByMonth = photos.groupBy {
                it.dateTaken.atZone(ZoneOffset.UTC).month
            }

            groupedByMonth.forEach { (month, photos) ->
                // add year if not matching with current year
                val title = if (yearOfFirst == yearNow) {
                    DateFormatSymbols().months[month.value - 1]
                } else {
                    "${DateFormatSymbols().months[month.value - 1]} $yearOfFirst"
                }


                // month header
                item(span = { GridItemSpan(4) }) {
                    Text(text = title, style = MaterialTheme.typography.bodyLarge)
                }

                items(photos.size) { index: Int ->
                    // TODO: show always local uri?
                    val data = if (photos[index].uri != null) {
                        photos[index].uri
                    } else {
                        photos[index].imageUrl
                    }

                    Image(
                        painter = rememberImagePainter(
                            data  = data,
                            builder = {
                                crossfade(true)
                                placeholder(R.drawable.image_placeholder)
                            }
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clickable {
                                onSelectItem(photos[index].filename)
                            }
                            .padding(1.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .size(128.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewPhotoGrid() {
    val list = (0..15).map {
        Photo(filename = it.toString(), imageUrl = "", Instant.parse("2022-01-01T13:37:00.123Z"))
    }
    AppTheme {
        PhotoGrid(
            photos = list,
            onSelectItem = {}
        )
    }
}
