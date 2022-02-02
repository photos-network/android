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
package photos.network

import android.app.Application
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import photos.network.data.BuildConfig
import photos.network.data.dataModule
import photos.network.domain.domainModule

/**
 * Android Application subclass to manually initialize logging and dependency injection.
 */
open class PhotosNetworkApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        // setup logging
        AndroidLogcatLogger.installOnDebuggableApp(
            this@PhotosNetworkApplication,
            minPriority = LogPriority.VERBOSE
        )

        // setup dependency injection
        startKoin {
            // Koin Android logger
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)

            // inject Android context
            androidContext(applicationContext)

            // use modules
            modules(
                listOf(
                    dataModule,
                    domainModule,
                    appModule,
                )
            )
        }
    }
}
