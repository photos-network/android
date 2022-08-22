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
package photos.network.data.user.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.parameter
import io.ktor.client.request.port
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.first
import logcat.LogPriority
import logcat.asLog
import logcat.logcat
import photos.network.data.user.network.model.TokenInfo
import photos.network.data.settings.repository.SettingsRepository
import photos.network.data.user.network.model.ApiResponse
import photos.network.data.user.network.model.NetworkUser
import photos.network.data.user.persistence.User
import photos.network.data.user.persistence.UserStorage

class UserApiImpl(
    private val httpClient: HttpClient,
    private val userStorage: UserStorage,
    private val settingsRepository: SettingsRepository,
): UserApi {
    override suspend fun verifyServerHost(host: String): Boolean {
        try {
            val response: HttpResponse = httpClient.request("$host/api") {
                method = HttpMethod.Get
            }

            if (!response.status.isSuccess()) {
                return false
            }

            val body = response.body<ApiResponse>()

            return body.message.contains("API running")

        } catch (exception: Exception) {
            logcat(LogPriority.ERROR) { exception.asLog() }

            return false
        }
    }

    override suspend fun verifyClientId(clientId: String): Boolean {
        val host = settingsRepository.settings.first().host
        val response: HttpResponse = httpClient.request("$host/api/oauth/authorize") {
            method = HttpMethod.Get
            parameter("client_id", clientId)
            parameter("redirect_uri", "photosapp://authenticate")
        }

        return response.status != HttpStatusCode.PreconditionFailed
    }

    override suspend fun accessTokenRequest(authCode: String): Boolean {
        val host = settingsRepository.settings.first().host
        val clientId = settingsRepository.settings.first().clientId

        val url = "$host/api/oauth/token"
        val tokenInfo: TokenInfo = httpClient.submitForm(
            url = url,
            formParameters = Parameters.build {
                append("grant_type", "authorization_code")
                append("code", authCode)
                append("redirect_uri", "photosapp://authenticate")
                append("client_id", clientId)
            }
        ).body()

        if (tokenInfo.accessToken.isEmpty() || tokenInfo.refreshToken.isEmpty()) {
            return false
        }

        val user = userStorage.read()
        val tmpUser: User
        if (user == null) {
            tmpUser = User(
                id = "",
                lastname = "",
                firstname = "",
                profileImageUrl = "",
                accessToken = tokenInfo.accessToken,
                refreshToken = tokenInfo.refreshToken,
            )
        } else {
            tmpUser = User(
                id = user.id,
                lastname = user.lastname,
                firstname = user.firstname,
                profileImageUrl = user.profileImageUrl,
                accessToken = tokenInfo.accessToken,
                refreshToken = tokenInfo.refreshToken,
            )
        }
        userStorage.save(tmpUser)

        return true
    }

    override suspend fun revocationRequest(
        token: String,
        tokenTypeHint: String?
    ) {
        val host = settingsRepository.settings.first().host

        httpClient.submitForm(
            url = "$host/api/revoke",
            formParameters = Parameters.build {
                append("token", token)
            }
        ) {
            this.port = port
        }
    }

    override suspend fun getUser(): NetworkUser? {
        try {
            val host = settingsRepository.settings.first().host

            return httpClient.request(urlString = "$host/api/user/") {
                method = HttpMethod.Get
            }.body<NetworkUser>()
        } catch (exception: Exception) {
            logcat(LogPriority.ERROR) { "Could not get user: $exception" }
        }
        return null
    }
}
