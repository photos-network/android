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
package photos.network.repository.photos

import android.app.Application
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import logcat.LogPriority
import logcat.logcat
import photos.network.api.photo.PhotoApi
import photos.network.database.photos.PhotoDao
import photos.network.repository.photos.model.Box
import photos.network.repository.photos.worker.SyncLocalPhotosWorker
import photos.network.repository.photos.worker.SyncStatus
import photos.network.system.mediastore.MediaStore
import java.time.Instant
import java.util.concurrent.TimeUnit

class PhotoRepositoryImpl(
    private val application: Application,
    private val mediaStore: MediaStore,
    private val photoApi: PhotoApi,
    private val photoDao: PhotoDao,
    private val workManager: WorkManager,
    private val assetManager: AssetManager,
) : PhotoRepository {
    // TODO: user should be able to define the required network type in the app settings.
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .build()

    private val periodicWorkRequest = PeriodicWorkRequest.Builder(
        SyncLocalPhotosWorker::class.java,
        2,
        TimeUnit.HOURS,
    )
        .setConstraints(constraints)
        .addTag("photosSyncWorker")
        .build()

    override suspend fun syncPhotos(): SyncStatus {
        val photos = mediaStore.queryLocalImages()
        logcat(LogPriority.VERBOSE) { "Found ${photos.size} photos." }

        photos.forEach {
            addPhoto(
                photo = Photo(
                    filename = it.name,
                    imageUrl = "",
                    dateAdded = Instant.now(),
                    dateTaken = it.dateTaken,
                    dateModified = null,
                    isPrivate = false,
                    uri = it.uri,
                ),
            )
        }

        // TODO: move to somewhere else
//        val syncLocalPhotosWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<SyncLocalPhotosWorker>().build()
//
//        WorkManager.getInstance(applicationContext)
//            .enqueue(syncLocalPhotosWorkRequest)

        return SyncStatus.SyncSucceeded
    }

    fun startPersiodicSync() {
        workManager.enqueueUniquePeriodicWork(
            "photosSyncWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest,
        )

        // TODO: observe sync state (at least errors)
        workManager.getWorkInfosByTag("photosSyncWorker")
    }

    override fun getPhotos(): Flow<List<Photo>> = photoDao.getPhotos().mapNotNull { photos ->
        photos.sortedByDescending {
            it.dateTaken ?: it.dateAdded
        }.map { photo ->
            Photo(photo)
        }
    }

    override fun getPhoto(identifier: String): Flow<Photo?> =
        photoDao.getPhoto(identifier).mapNotNull {
            it?.let { it1 -> Photo(it1) }
        }

    override suspend fun addPhoto(photo: Photo) {
        photoDao.insertAll(photos = arrayOf(photo.toDatabasePhoto()))
    }

    override fun getFaces(photoUri: Uri): Flow<List<Box>> {
        return flow {
            emit(emptyList())

            val bitmap: Bitmap = android.provider.MediaStore.Images.Media.getBitmap(application.contentResolver, photoUri)

            val faces = MTCNN(assetManager).detectFaces(
                bitmap,
                40,
            )

            emit(faces.map { box -> box })
        }
    }
}
