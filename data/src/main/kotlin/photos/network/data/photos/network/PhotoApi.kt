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
package photos.network.data.photos.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import kotlinx.coroutines.flow.first
import photos.network.data.settings.repository.SettingsRepository

class PhotoApi(
    private val httpClient: HttpClient,
    private val settingsRepository: SettingsRepository,
) {
    /**
     * List all photos for current User has access to
     */
    suspend fun getPhotos(
        offset: Int = 0,
        limit: Int = 0,
    ): Photos {
        val host = settingsRepository.settings.first().host
        return httpClient.request(urlString = "$host/api/photos") {
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

    /**
     * Load detailed information for a single photo
     *
     * @param photoId Identifier of the photo details to return
     */
    suspend fun getPhoto(photoId: String): Photo =
        httpClient.request(urlString = "/api/photo/$photoId").body()
}
