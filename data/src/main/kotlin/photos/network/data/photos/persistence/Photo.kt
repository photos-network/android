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
package photos.network.data.photos.persistence

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import photos.network.data.photos.repository.Photo as RepositoryPhoto

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey val filename: String,
    val imageUrl: String,
    val dateTaken: Long,
    val uri: String? = null
) {
    fun toPhoto(): RepositoryPhoto {
        return RepositoryPhoto(
            filename = filename,
            imageUrl = imageUrl,
            dateTaken = Instant.ofEpochMilli(dateTaken),
            uri = uri?.let { Uri.parse(it) },
        )
    }
}
