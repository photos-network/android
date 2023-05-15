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
package photos.network

import android.app.Application
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import photos.network.api.apiModule
import photos.network.database.photos.databasePhotosModule
import photos.network.database.settings.databaseSettingsModule
import photos.network.database.sharing.databaseSharingModule
import photos.network.domain.albums.domainAlbumsModule
import photos.network.domain.folders.domainFoldersModule
import photos.network.domain.photos.domainPhotosModule
import photos.network.domain.settings.domainSettingsModule
import photos.network.domain.sharing.domainSharingModule
import photos.network.repository.folders.repositoryFoldersModule
import photos.network.repository.photos.repositoryPhotosModule
import photos.network.repository.settings.repositorySettingsModule
import photos.network.repository.sharing.repositorySharingModule
import photos.network.system.filesystem.systemFilesystemModule
import photos.network.system.mediastore.systemMediastoreModule
import photos.network.ui.albums.uiAlbumsModule
import photos.network.ui.folders.uiFoldersModule
import photos.network.ui.photos.uiPhotosModule
import photos.network.ui.settings.uiSettingsModule
import photos.network.ui.sharing.uiSharingModule

/**
 * Android Application subclass to manually initialize logging and dependency injection.
 */
open class PhotosNetworkApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        // setup logging
        AndroidLogcatLogger.installOnDebuggableApp(
            this@PhotosNetworkApplication,
            minPriority = LogPriority.VERBOSE,
        )

        // setup dependency injection
        startKoin {
            // Koin Android logger
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)

            // inject Android context
            androidContext(applicationContext)

            workManagerFactory()

            // use modules
            modules(
                listOf(
                    appModule,

                    // albums
                    uiAlbumsModule,
                    domainAlbumsModule,

                    // folders
                    uiFoldersModule,
                    domainFoldersModule,
                    repositoryFoldersModule,

                    // photos
                    uiPhotosModule,
                    domainPhotosModule,
                    repositoryPhotosModule,
                    databasePhotosModule,

                    // settings
                    uiSettingsModule,
                    domainSettingsModule,
                    repositorySettingsModule,
                    databaseSettingsModule,

                    // sharing
                    uiSharingModule,
                    domainSharingModule,
                    repositorySharingModule,
                    databaseSharingModule,

                    apiModule,

                    systemFilesystemModule,
                    systemMediastoreModule,
                ),
            )
        }
    }
}
