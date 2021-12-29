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
package network.photos.android.data.photos.network

import network.photos.android.data.photos.domain.PhotoElement
import network.photos.android.data.photos.network.model.PhotosResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface PhotoApi {

    /**
     * List all photos for current User has access to
     */
    @Headers("Accept: application/json")
    @GET("api/photos")
    suspend fun getPhotos(
        @Header("Authorization") authToken: String?,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 50
    ): PhotosResponse

    /**
     * Load detailed information for a single photo
     *
     * @param photoId Identifier of the photo details to return
     */
    @Headers("Accept: application/json")
    @GET("api/photo/{photoId}")
    suspend fun getPhoto(photoId: String): PhotoElement
}
