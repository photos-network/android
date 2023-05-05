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
package photos.network.api.photo.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("owner") val owner: String? = null,
    @SerialName("date_added") val dateAdded: String? = null,
    @SerialName("date_taken") val dateTaken: String? = null,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("details") val details: String? = null,
    @SerialName("tags") val tags: String? = null,
    @SerialName("location") val location: String? = null,
)
