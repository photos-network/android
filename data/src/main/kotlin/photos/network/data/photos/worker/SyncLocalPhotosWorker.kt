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
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.io.InputStream
import java.time.Instant
import logcat.LogPriority
import logcat.logcat
import photos.network.data.photos.repository.Photo
import photos.network.data.photos.repository.PhotoRepository

/**
 * Synchronizes all local photos from androids media store with the local database.
 */
class SyncLocalPhotosWorker(
    application: Application,
    workerParameters: WorkerParameters,
    private val photoRepository: PhotoRepository,
) : CoroutineWorker(application.applicationContext, workerParameters) {
    override suspend fun doWork(): Result {
        val photos = queryLocalMediaStore()
        logcat(LogPriority.VERBOSE) { "Found ${photos.size} photos." }

        photos.forEach {
            photoRepository.addPhoto(it)
        }

        return Result.success()
    }

    private fun generateContentUri(): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
    }

    private fun generateProjection(): Array<String> {
        val projection = mutableListOf<String>()

        projection += MediaStore.Images.Media._ID
        projection += MediaStore.Images.Media.DISPLAY_NAME
        projection += MediaStore.Images.Media.SIZE
        projection += MediaStore.Images.Media.DATE_TAKEN

        // deprecated with 29 (Q)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            projection += MediaStore.Images.Media.LATITUDE
            projection += MediaStore.Images.Media.LONGITUDE
        }

        // added with 30 (R)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            projection += MediaStore.Images.Media.F_NUMBER
            projection += MediaStore.Images.Media.ISO
            projection += MediaStore.Images.Media.EXPOSURE_TIME
        }

        return projection.toTypedArray()
    }

    /**
     * Query images from Androids local MediaStore (`DCIM/` and `Pictures/`)
     */
    private fun queryLocalMediaStore(): List<Photo> {
        val photos = mutableListOf<Photo>()

        val selection = null
        val selectionArgs = null
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        applicationContext.contentResolver.query(
            generateContentUri(),
            generateProjection(),
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

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

            logcat(LogPriority.ERROR) { "count: ${cursor.count}" }

            while (cursor.moveToNext()) {
                var photoUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    cursor.getString(idColumn)
                )

                // Get values of columns for a given Image.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(sizeColumn)
                val dateTaken = cursor.getLong(dateTakenColumn)

                val exposure: String? = if (exposureColumn != -1) {
                    cursor.getString(exposureColumn)
                } else {
                    null
                }

                val fnumber: String? = if (fnumberColumn != -1) {
                    cursor.getString(fnumberColumn)
                } else {
                    null
                }

                val isoNumber = if (isoNumberColumn != -1) {
                    cursor.getString(isoNumberColumn)
                } else {
                    null
                }

                // Image location
                var latLong = FloatArray(2)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    photoUri = MediaStore.setRequireOriginal(photoUri)
                    val stream: InputStream? =
                        applicationContext.contentResolver.openInputStream(photoUri)
                    if (stream == null) {
                        logcat(LogPriority.WARN) { "Got a null input stream for $photoUri" }
                        continue
                    }

                    val exifInterface = ExifInterface(stream)
                    // If it returns null, fall back to {0.0, 0.0}.
                    exifInterface.getLatLong(latLong)

                    stream.close()
                } else {
                    if (latColumn != -1 && longColumn != -1) {
                        latLong = floatArrayOf(
                            cursor.getFloat(latColumn),
                            cursor.getFloat(longColumn)
                        )
                    }
                }

                logcat(priority = LogPriority.ERROR) { "details: exposure=$exposure, fnumber=$fnumber, isoNumber=$isoNumber, lat=${latLong[0]}, lon=${latLong[0]}" }
                // TODO: add file details?
                photos += Photo(
                    filename = name,
                    imageUrl = name,
                    dateTaken = Instant.ofEpochMilli(dateTaken),
                    uri = photoUri,
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
