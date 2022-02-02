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
package photos.network.data.user.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import logcat.LogPriority
import logcat.logcat
import photos.network.data.settings.repository.SettingsRepository
import photos.network.data.user.network.model.NetworkUser

class UserApi(
    private val httpClient: HttpClient,
    val settingsRepository: SettingsRepository,
) {
    suspend fun getUser(): NetworkUser? {
        try {
            logcat(LogPriority.WARN) { "try to request ${settingsRepository.host}/api/user" }
            httpClient.get<NetworkUser>("${settingsRepository.host}/api/user/") {
                logcat(LogPriority.ERROR) { "request: ${this.url.protocol} ${this.url.host} ${this.url.encodedPath}" }
            }
//            httpClient.get(path = "/api/user/")
        } catch (exception: Exception) {
            logcat(LogPriority.ERROR) { "Could not get user: $exception" }
        }
        return null
    }

    suspend fun requestAutorization(authCode: String, clientId: String) = httpClient.post<Unit>(path = "${settingsRepository.host}/api/")

    /**
     * OAuth token revocation based on [RFC7000](https://tools.ietf.org/html/rfc7009#section-2.1)
     *
     * @param token The token to revoke on the oauth server.
     * @param tokenTypeHint This optional param should either be 'access_token' or 'refresh_token'.
     */
    suspend fun revoke(
        token: String,
        tokenTypeHint: String?
    ) = httpClient.post<Unit>(path = "/api/revoke")
}
