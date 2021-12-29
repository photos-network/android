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
package network.photos.android.data.photos.repository

import android.util.Log
import network.photos.android.data.Resource
import network.photos.android.data.photos.domain.PhotoElement
import network.photos.android.data.photos.network.PhotoApi
import network.photos.android.data.user.domain.Token

class PhotoRepositoryImpl(
    private val photoApi: PhotoApi
) : PhotoRepository {
    override suspend fun getPhotos(
        token: Token?,
        page: Int
    ): Resource<List<PhotoElement>> {
        try {
            val response = photoApi.getPhotos(authToken = "Bearer ${token?.accessToken}")
            return Resource.Success(
                data = response.results.map {
                    PhotoElement(
                        id = it.id,
                        name = it.name,
                        owner = null,
                        created_at = null,
                        modified_at = null,
                        details = null,
                        tags = null,
                        location = null,
                        image_url = it.imageUrl,
                    )
                }
            )
        } catch (e: Exception) {
            Log.e("TAG", "HTTP Client Error", e)
        }
        return Resource.Error("HTTP Client Error")
    }
}
