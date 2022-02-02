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

import android.content.ContentUris
import android.content.Context
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import logcat.LogPriority
import logcat.logcat
import photos.network.data.photos.persistence.entities.DatabasePhoto
import photos.network.data.photos.repository.PhotoRepository
import java.io.InputStream

/**
 * Reads all local photos from androids content provider,
 * add these into database and if not synced with backend, upload them.
 */
class PhotosSyncWork(
    context: Context,
    workerParameters: WorkerParameters,
    private val photoRepository: PhotoRepository,
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val photos = queryLocalPhotos()
        logcat(LogPriority.VERBOSE) { "Found ${photos.size} photos." }

        // TODO: add images into database (if not exist)
        photoRepository.getPhotos().collect {
            logcat(LogPriority.ERROR) { "getPhotos.collect ${it.data?.count()}" }
        }

        // TODO: download list of online images
        // TODO: upload not synced images (synced=false)

        logcat(LogPriority.ERROR) { "do work()..." }

        for (i in 0..5) {
            delay(2_000)
            logcat(LogPriority.ERROR) { "do work() $i" }
        }

        logcat(LogPriority.ERROR) { "do work() done" }

        return Result.failure()
    }

    /**
     * Find all local photographs in `DCIM/` and `Pictures/`
     */
    private fun queryLocalPhotos(): List<DatabasePhoto> {
        val photos = mutableListOf<DatabasePhoto>()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        var projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            projection += MediaStore.Images.Media.F_NUMBER
            projection += MediaStore.Images.Media.ISO
            projection += MediaStore.Images.Media.EXPOSURE_TIME
            projection += MediaStore.Images.Media.LATITUDE
            projection += MediaStore.Images.Media.LONGITUDE
        }

        val selection = null
        val selectionArgs = null
//        arrayOf(
//            TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString()
//        )

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        applicationContext.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
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

            while (cursor.moveToNext()) {
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
                    photoId = 0,
                    filename = name,
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
        }

        return photos
    }
}
