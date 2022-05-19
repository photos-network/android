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
package photos.network.data.photos.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import photos.network.data.photos.network.PhotoApi
import photos.network.data.photos.persistence.PhotoDao
import photos.network.data.photos.worker.SyncLocalPhotosWorker
import java.util.concurrent.TimeUnit

class PhotoRepositoryImpl(
    private val applicationContext: Context,
    private val photoApi: PhotoApi,
    private val photoDao: PhotoDao,
    private val workManager: WorkManager,
) : PhotoRepository {
    // TODO: user should be able to define the required network type in the app settings.
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .build()

    private val periodicWorkRequest = PeriodicWorkRequest.Builder(
        SyncLocalPhotosWorker::class.java, 2, TimeUnit.HOURS
    )
        .setConstraints(constraints)
        .addTag("photosSyncWorker")
        .build()

    override fun syncPhotos() {
        val syncLocalPhotosWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<SyncLocalPhotosWorker>().build()

        WorkManager.getInstance(applicationContext)
            .enqueue(syncLocalPhotosWorkRequest)
    }

    fun startPersiodicSync() {
        workManager.enqueueUniquePeriodicWork(
            "photosSyncWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )

        // TODO: observe sync state (at least errors)
        workManager.getWorkInfosByTag("photosSyncWorker")
    }

    override fun getPhotos(): Flow<List<Photo>> = photoDao.getPhotos().mapNotNull { photos ->
        photos.sortedByDescending {
            it.dateTaken ?: it.dateAdded
        }.map { photo ->
            photo.toPhoto()
        }
    }

    override fun getPhoto(identifier: String): Flow<Photo?> = photoDao.getPhoto(identifier).mapNotNull {
        it?.toPhoto()
    }

    override suspend fun addPhoto(photo: Photo) {
        photoDao.insertAll(photos = arrayOf(photo.toDatabasePhoto()))
    }
}
