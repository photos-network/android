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
package photos.network.database.photos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    val uuid: String? = null, // will be generated on the backend
    @PrimaryKey val filename: String,
    val imageUrl: String,
    val dateAdded: Long,
    val dateTaken: Long? = null,
    val dateModified: Long? = null,
    val thumbnailFileUri: String? = null,
    val originalFileUri: String? = null,
)
