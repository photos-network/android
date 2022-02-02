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
package photos.network.domain.photos.model

data class PhotoElement(
    val id: String? = null,
    val name: String,
    val owner: String?,
    val created_at: String?,
    val modified_at: String?,
    val details: TechnicalDetails?,
    val tags: List<String>?,
    val location: Location?,
    val image_url: String
)
