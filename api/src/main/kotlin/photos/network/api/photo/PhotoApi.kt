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
package photos.network.api.photo

import photos.network.api.photo.entity.Photo

interface PhotoApi {
    /**
     * List all photos for current User has access to
     */
    suspend fun getPhotos(
        offset: Int = 0,
        limit: Int = 0,
    ): Photos

    /**
     * Load detailed information for a single photo
     *
     * @param photoId Identifier of the photo details to return
     */
    suspend fun getPhoto(photoId: String): Photo
}
