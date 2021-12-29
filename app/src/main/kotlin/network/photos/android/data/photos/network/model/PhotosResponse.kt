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
package network.photos.android.data.photos.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotosResponse(
    @SerialName("offset") val offset: Int,
    @SerialName("limit") val limit: Int,
    @SerialName("size") val size: Int,
    @SerialName("results") val results: List<PhotoItem>,
)

@Serializable
data class PhotoItem(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("date_added") val dateAdded: String? = null,
    @SerialName("date_taken") val dateTaken: String? = null,
)
