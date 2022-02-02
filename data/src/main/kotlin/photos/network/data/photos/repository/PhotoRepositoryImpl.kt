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
package photos.network.data.photos.repository

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.io.InputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import logcat.LogPriority
import logcat.logcat
import photos.network.data.Resource
import photos.network.data.photos.entities.Photo
import photos.network.data.photos.network.PhotoApi
import photos.network.data.photos.network.toPhoto
import photos.network.data.photos.worker.PhotosSyncWork
import java.util.concurrent.TimeUnit
import photos.network.data.photos.persistence.entities.DatabasePhoto
import photos.network.data.photos.persistence.entities.toPhoto

class PhotoRepositoryImpl(
    private val applicationContext: Context,
    private val photoApi: PhotoApi,
//    private val localDataSource: PhotoDataSource,
    private val workManager: WorkManager,
) : PhotoRepository {
    // TODO: user should be able to define the required network type in the app settings.
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .build()

    private val periodicWorkRequest = PeriodicWorkRequest.Builder(
        PhotosSyncWork::class.java, 2, TimeUnit.HOURS
    )
        .setConstraints(constraints)
        .addTag("photosSyncWorker")
        .build()

    override suspend fun syncPhotos() {
        workManager.enqueueUniquePeriodicWork(
            "photosSyncWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )

        // TODO: observe sync state (at least errors)
        workManager.getWorkInfosByTag("photosSyncWorker")
    }

    override suspend fun getPhotos(): Flow<Resource<List<Photo>>> = flow<Resource<List<Photo>>> {
        // TODO: return cached images
        // TODO: return merged with lists from photos.network instance

        // load from local MediaStore
        try {
            val deferred = withContext(Dispatchers.IO + NonCancellable) {
                async {
                    queryMediaStore()
                }
            }

            emit(Resource.Success(deferred.await().map { it.toPhoto() }))
        } catch (e: RuntimeException) {
            logcat(LogPriority.WARN) { "Could not fetch photos from Media Store!" }
            emit(Resource.Error("Could not fetch photos from Media Store!"))
        }
    }.flowOn(Dispatchers.IO)

    private fun createMediaStoreCursor() : Cursor? {
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        var projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
        )

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                projection += MediaStore.Images.Media.F_NUMBER
                projection += MediaStore.Images.Media.ISO
                projection += MediaStore.Images.Media.EXPOSURE_TIME
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                projection += MediaStore.Images.Media.DATE_TAKEN
            }
            else -> {
                projection += MediaStore.Images.Media.LATITUDE
                projection += MediaStore.Images.Media.LONGITUDE
            }
        }

        val selection = null
        val selectionArgs = null
//        arrayOf(
//            TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString()
//        )

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        return applicationContext.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
    }

    private var startingRow = 0
    private var rowsToLoad = 0

    /**
     * query images from Androids local MediaStore
     */
    private suspend fun queryMediaStore(): List<DatabasePhoto> {
        val photos = mutableListOf<DatabasePhoto>()

        createMediaStoreCursor()?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val fnumberColumn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.F_NUMBER)
            } else {
                -1
            }

            val isoNumberColumn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ISO)
            } else {
                -1
            }

            val exposureColumn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.EXPOSURE_TIME)
            } else {
                -1
            }

            val dateTakenColumn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            } else {
                -1
            }

            val latColumn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE)
            } else {
                -1
            }
            val longColumn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                -1
            } else {
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE)
            }

            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

            logcat(LogPriority.ERROR) { "count: ${cursor.count}" }

            // TODO: add paging
            for (i in 0..20) {
                cursor.moveToPosition(i)
//            while (cursor.moveToNext()) {
                var photoUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    cursor.getString(idColumn)
                )

                // Get values of columns for a given Image.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val dateTaken = cursor.getLong(dateTakenColumn)
                val exposure = cursor.getString(exposureColumn)
                val fnumber = cursor.getString(fnumberColumn)
                val isoNumber = cursor.getString(isoNumberColumn)

                // Image location
                var latLong = FloatArray(2)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    photoUri = MediaStore.setRequireOriginal(photoUri)
                    val stream: InputStream? = applicationContext.contentResolver.openInputStream(photoUri)
                    if (stream == null) {
                        logcat(LogPriority.WARN) { "Got a null input stream for $photoUri" }
                        continue
                    }

                    val exifInterface = ExifInterface(stream)
                    // If it returns null, fall back to {0.0, 0.0}.
                    exifInterface.getLatLong(latLong)

                    stream.close()
                } else {
                    latLong = floatArrayOf(
                        cursor.getFloat(latColumn),
                        cursor.getFloat(longColumn)
                    )
                }

                val size = cursor.getInt(sizeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                // TODO: add file details?
                photos += DatabasePhoto(
                    photoId = id,
                    filename = contentUri.toString(),
                    dateTaken = dateTaken,
//                    details = TechnicalDetails(
//                        exposure = exposure,
//                        focal_length = fnumber,
//                        iso = isoNumber,
//                    ),
//                    tags = listOf(),
//                    location = Location(latLong[0], latLong[1], 0),
//                    image_url = "",
                )
            }

            startingRow = rowsToLoad
        }

        return photos
    }
}
