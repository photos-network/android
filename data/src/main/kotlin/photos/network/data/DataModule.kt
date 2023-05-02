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
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
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
import photos.network.data.user.network.model.TokenInfo
import photos.network.data.user.persistence.User
import photos.network.data.user.persistence.UserStorage
import photos.network.data.user.repository.UserRepository
import photos.network.data.user.repository.UserRepositoryImpl

val dataModule = module {

    single<UserRepository> {
        UserRepositoryImpl(
            userApi = get(),
            userStorage = get()
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
