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
package photos.network.network

import android.app.Application
import android.content.pm.PackageInfo
import android.os.Build
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.plugins.plugin
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.headers
import io.ktor.client.request.port
import io.ktor.client.request.url
import io.ktor.client.statement.request
import io.ktor.http.Parameters
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import logcat.LogPriority
import logcat.logcat
import org.koin.dsl.module
import photos.network.common.persistence.SecureStorage
import photos.network.common.persistence.Settings
import photos.network.common.persistence.User
import photos.network.network.photo.PhotoApi
import photos.network.network.photo.PhotoApiImpl
import photos.network.network.user.UserApi
import photos.network.network.user.UserApiImpl
import photos.network.network.user.model.TokenInfo

val networkModule = module {
    single {
        provideKtorClient(
            application = get(),
            userStorage = get(),
            settingsStore = get(),
        )
    }

    single<UserApi> {
        UserApiImpl(
            httpClient = get(),
            settingsStorage = get(),
            userStorage = get(),
        )
    }

    single<PhotoApi> {
        PhotoApiImpl(httpClient = get())
    }
}

@Suppress("LongMethod")
private fun provideKtorClient(
    application: Application,
    userStorage: SecureStorage<User>,
    settingsStore: SecureStorage<Settings>,
): HttpClient {
    val client = HttpClient(CIO) {
        expectSuccess = false
        followRedirects = true

        defaultRequest {
            headers {
                val context = application.applicationContext

                @Suppress("DEPRECATION")
                val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                val version: String = packageInfo.versionName

                @Suppress("DEPRECATION")
                val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    packageInfo.longVersionCode
                } else {
                    packageInfo.versionCode
                }

                append(
                    "User-Agent",
                    "PhotosNetwork-Android/$version (Build $versionCode) Android/${Build.VERSION.RELEASE}",
                )
            }
        }

        engine {
            @Suppress("MagicNumber")
            threadsCount = 1_000
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    logcat(LogPriority.INFO) { message }
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse {
                logcat(LogPriority.INFO) { "<== ${it.status} ${it.request.url}" }
            }
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                },
            )
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = userStorage.read()?.accessToken ?: ""
                    val refreshToken = userStorage.read()?.refreshToken ?: ""

                    BearerTokens(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                    )
                }

                // called after receiving a 401 (Unauthorized) response with the WWW-Authenticate header
                refreshTokens {
                    val refreshToken = userStorage.read()?.refreshToken ?: ""
                    val host = settingsStore.read()?.host ?: ""
                    val clientId = settingsStore.read()?.clientId ?: ""

                    /**
                     * OAuth refresh token request based on [RFC6749](https://tools.ietf.org/html/rfc6749#section-6)
                     *
                     * @param refreshToken The refresh token issued to the client.
                     * @param clientId The client identifier issued by the authorization server.
                     * @param scope list of case-sensitive strings to grant access based on.
                     */
                    /**
                     * OAuth refresh token request based on [RFC6749](https://tools.ietf.org/html/rfc6749#section-6)
                     *
                     * @param refreshToken The refresh token issued to the client.
                     * @param clientId The client identifier issued by the authorization server.
                     * @param scope list of case-sensitive strings to grant access based on.
                     */
                    val refreshTokenInfo: TokenInfo = client.submitForm(
                        url = "$host/api/oauth/token",
                        formParameters = Parameters.build {
                            append("grant_type", "refresh_token")
                            append("refresh_token", refreshToken)
                            append("client_id", clientId)
                            append("scope", "openid profile email phone library:read library:write")
                        },
                    ).body()

                    logcat(LogPriority.ERROR) { "refreshTokenInfo=${refreshTokenInfo.accessToken}" }

                    val user = userStorage.read()
                    user?.let {
                        val tmpUser = User(
                            id = it.id,
                            lastname = it.lastname,
                            firstname = it.firstname,
                            profileImageUrl = it.profileImageUrl,
                            accessToken = refreshTokenInfo.accessToken,
                            refreshToken = refreshTokenInfo.refreshToken,
                        )
                        userStorage.save(tmpUser)
                    }

                    BearerTokens(
                        accessToken = refreshTokenInfo.accessToken,
                        refreshToken = refreshTokenInfo.refreshToken,
                    )
                }

                // always send credentials when predicate fulfilled
                sendWithoutRequest { request ->
                    request.url.encodedPath.endsWith("/protected")
                }
            }
        }
    }

    client.plugin(HttpSend).intercept { request ->
        // replace port and host for each call
        @Suppress("MagicNumber")
        request.port = settingsStore.read()?.port ?: 443
        request.url(settingsStore.read()?.host ?: "")

        val originalCall = execute(request)

        originalCall
    }

    return client
}
