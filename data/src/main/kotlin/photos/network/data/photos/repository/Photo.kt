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
package photos.network.data.photos.repository

import android.net.Uri
import java.time.Instant
import photos.network.data.photos.persistence.Photo as DatabasePhoto

data class Photo(
    val filename: String,
    val imageUrl: String,
    val dateTaken: Instant,
    val uri: Uri? = null,
) {
    fun toDatabasePhoto(): DatabasePhoto = DatabasePhoto(
        filename = filename,
        imageUrl = imageUrl,
        dateTaken = dateTaken.toEpochMilli(),
        uri = uri.toString(),
    )
}
