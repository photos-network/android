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
package photos.network.data

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.request
import io.ktor.http.Parameters
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import logcat.LogPriority
import logcat.logcat
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import photos.network.data.photos.network.PhotoApi
import photos.network.data.photos.network.TokenInfo
import photos.network.data.photos.network.PhotoApiImpl
import photos.network.data.photos.persistence.MIGRATION_1_2
import photos.network.data.photos.persistence.PhotoDao
import photos.network.data.photos.persistence.PhotoDatabase
import photos.network.data.photos.repository.PhotoRepository
import photos.network.data.photos.repository.PhotoRepositoryImpl
import photos.network.data.photos.worker.SyncLocalPhotosWorker
import photos.network.data.settings.persistence.SettingsStorage
import photos.network.data.settings.repository.SettingsRepository
import photos.network.data.settings.repository.SettingsRepositoryImpl
import photos.network.data.user.network.UserApi
import photos.network.data.user.network.UserApiImpl
import photos.network.data.user.persistence.User
import photos.network.data.user.persistence.UserStorage
import photos.network.data.user.repository.UserRepository
import photos.network.data.user.repository.UserRepositoryImpl

val dataModule = module {
    single {
        UserApiImpl(
            httpClient = get(),
            settingsRepository = get(),
            userStorage = get(),
        )
    }

    single { UserStorage(context = get()) }

    factory { WorkManager.getInstance(androidApplication()) }

    worker {
        SyncLocalPhotosWorker(
            application = get(),
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

    single {
        provideKtorClient(
            userStorage = get(),
            settingsStore = get(),
        )
    }

    single {
        PhotoApiImpl(
            httpClient = get(),
            settingsRepository = get(),
        )
    }

    single<PhotoRepository> {
        PhotoRepositoryImpl(
            applicationContext = get(),
            photoApi = get(),
            photoDao = get(),
            workManager = get(),
        )
    }
    single { providePhotoDatabase(get()) }
    factory { providePhotoDao(get()) }

    single {
        SettingsStorage(context = get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            settingsStore = get(),
        )
    }
}

private fun providePhotoDatabase(context: Context): PhotoDatabase {
    return Room.databaseBuilder(
        context,
        PhotoDatabase::class.java, "photos.db"
    )
        .addMigrations(MIGRATION_1_2)
        .build()
}

private fun providePhotoDao(photoDatabase: PhotoDatabase): PhotoDao {
    return photoDatabase.photoDao()
}

private fun provideKtorClient(
    userStorage: UserStorage,
    settingsStore: SettingsStorage,
): HttpClient {
    val client = HttpClient(Android) {
        expectSuccess = false
        followRedirects = true

        engine {
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
                }
            )
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = userStorage.read()?.accessToken ?: ""
                    val refreshToken = userStorage.read()?.refreshToken ?: ""

                    BearerTokens(
                        accessToken = accessToken,
                        refreshToken = refreshToken
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
                            refreshToken = refreshTokenInfo.refreshToken
                        )
                        userStorage.save(tmpUser)
                    }

                    BearerTokens(
                        accessToken = refreshTokenInfo.accessToken,
                        refreshToken = refreshTokenInfo.refreshToken
                    )
                }

                // always send credentials when predicate fulfilled
                sendWithoutRequest { request ->
                    request.url.encodedPath.endsWith("/protected")
                }
            }
        }
    }

    return client
}
