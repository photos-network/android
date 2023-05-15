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

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import photos.network.api.photo.entity.Photo

class PhotoApiImpl(
    private val httpClient: HttpClient,
) : PhotoApi {
    override suspend fun getPhotos(offset: Int, limit: Int): Photos {
        return httpClient.get(urlString = "/api/photos") {
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

    override suspend fun getPhoto(photoId: String): Photo =
        httpClient.get(urlString = "/api/photo/$photoId").body()
}
