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
package photos.network.data

import androidx.work.WorkManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.BearerTokens
import io.ktor.client.features.auth.providers.bearer
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.host
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.URLProtocol
import kotlinx.serialization.json.Json
import logcat.logcat
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import photos.network.data.photos.network.PhotoApi
import photos.network.data.photos.network.entity.TokenInfo
import photos.network.data.photos.repository.PhotoRepository
import photos.network.data.photos.repository.PhotoRepositoryImpl
import photos.network.data.photos.worker.PhotosSyncWork
import photos.network.data.settings.persistence.SettingsStorage
import photos.network.data.settings.repository.SettingsRepository
import photos.network.data.settings.repository.SettingsRepositoryImpl
import photos.network.data.user.network.UserApi
import photos.network.data.user.persistence.UserStorage
import photos.network.data.user.repository.UserRepository
import photos.network.data.user.repository.UserRepositoryImpl

val dataModule = module {
    factory { PhotosHttpClient(settingsRepository = get()) }

    single {
        UserApi(
            httpClient = PhotosHttpClient(
                settingsRepository = get()
            ).httpClient,
            settingsRepository = get()
        )
    }

    single { UserStorage(context = get()) }

    factory { WorkManager.getInstance(androidApplication()) }

    worker {
        PhotosSyncWork(
            context = get(),
            workerParameters = get(),
            photoRepository = get(),
        )
    }

    single<UserRepository> {
        UserRepositoryImpl(
            userApi = get(),
            userStorage = get()
        )
    }

    single { PhotoApi(httpClient = PhotosHttpClient(settingsRepository = get()).httpClient) }

    single<PhotoRepository> {
        PhotoRepositoryImpl(
            applicationContext = get(),
            photoApi = get(),
            workManager = get()
        )
    }

    single {
        SettingsStorage(context = get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            settingsStore = get()
        )
    }
}

class PhotosHttpClient(
    val settingsRepository: SettingsRepository
) {
    private val authCode: String
        get() = settingsRepository.authCode ?: ""
    private val clientId: String
        get() = settingsRepository.clientId ?: ""
    private val clientSecret: String
        get() = settingsRepository.clientSecret ?: ""
    private val redirectUri: String
        get() = settingsRepository.redirectUri ?: ""
    private val scope: String
        get() = settingsRepository.scope ?: ""
    private val host: String
        get() = settingsRepository.host ?: "photos.network"

    init {
        logcat { "DataModule init" }
        settingsRepository.loadSettings()
    }

    val httpClient = HttpClient(CIO) {
        expectSuccess = false
        followRedirects = true

//        defaultRequest {
//            method = HttpMethod.Get
//            host = settingsRepository.host ?: "https://photos.network"
//            url {
//                protocol = URLProtocol.HTTPS
//            }
//        }

        install(Auth) {
            lateinit var tokenInfo: TokenInfo
            var refreshTokenInfo: TokenInfo

            bearer {
                // called to get an initial token
                loadTokens {
                    logcat { "install(Auth) loadTokens" }
                    /**
                     * OAuth token request based on [RFC6749](https://tools.ietf.org/html/rfc6749#section-4.1.3)
                     *
                     * @param code The authorization code received from the authorization server.
                     * @param clientId The client identifier issued by the authorization server.
                     * @param redirectUri The redirect_uri included in the authorization request.
                     */
                    tokenInfo = tokenClient.submitForm(
                        formParameters = Parameters.build {
                            append("grant_type", "authorization_code")
                            append("code", this@PhotosHttpClient.authCode)
                            append("client_id", this@PhotosHttpClient.clientId)
                            append("redirect_uri", this@PhotosHttpClient.redirectUri)
                        },
                        path = "/api/oauth/token"
                    )
                    BearerTokens(
                        accessToken = tokenInfo.accessToken,
                        refreshToken = tokenInfo.refreshToken
                    )
                }

                // called after receiving a 401 (Unauthorized) response with the WWW-Authenticate header
                refreshTokens { unauthorizedResponse: HttpResponse ->
                    /**
                     * OAuth refresh token request based on [RFC6749](https://tools.ietf.org/html/rfc6749#section-6)
                     *
                     * @param refreshToken The refresh token issued to the client.
                     * @param clientId The client identifier issued by the authorization server.
                     * @param clientSecret The client secret issued during registration process.
                     * @param scope list of case-sensitive strings to grant access based on.
                     */
                    refreshTokenInfo = tokenClient.submitForm(
                        formParameters = Parameters.build {
                            append("grant_type", "refresh_token")
                            append("refresh_token", tokenInfo.refreshToken)
                            append("client_id", this@PhotosHttpClient.clientId)
                            append("client_secret", this@PhotosHttpClient.clientSecret)
                            append("scope", this@PhotosHttpClient.scope)
                        },
                        path = "/api/oauth/token"
                    )

                    BearerTokens(
                        accessToken = refreshTokenInfo.accessToken,
                        refreshToken = tokenInfo.refreshToken
                    )
                }

                // add calls
                sendWithoutRequest { request ->
                    false
                }
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
            connectTimeoutMillis = 60_000
            socketTimeoutMillis = 60_000
        }

        engine {
            requestTimeout = 60_000
            maxConnectionsCount = 1_000
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    prettyPrint = true
                }
            )
        }
    }

    private val tokenClient = HttpClient(CIO) {
        defaultRequest {
            method = HttpMethod.Get
            host = this@PhotosHttpClient.host
            url {
                protocol = URLProtocol.HTTPS
            }
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
            )
        }
    }
}
