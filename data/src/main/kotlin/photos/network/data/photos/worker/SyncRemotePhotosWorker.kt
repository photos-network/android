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
package photos.network.data.photos.worker

import android.app.Application
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import photos.network.data.photos.repository.PhotoRepository

/**
 * Synchronizes all remote photos from a photos.network instance with the local database.
 *
 * - Uploading local photos if not synced
 * * Downloading remote photos if not synced
 */
class SyncRemotePhotosWorker(
    application: Application,
    workerParameters: WorkerParameters,
    private val photoRepository: PhotoRepository,
) : CoroutineWorker(application.applicationContext, workerParameters) {
    override suspend fun doWork(): Result {
        // TODO: Not yet implemented!

        return Result.failure()
    }
}
