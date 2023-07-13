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
package photos.network.system.mediastore

import android.Manifest
import android.app.Application
import android.location.Location
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresPermission
import logcat.LogPriority
import logcat.asLog
import logcat.logcat
import java.io.InputStream
import java.time.Instant

class MediaStoreImpl(
    private val application: Application,
) : MediaStore {
    /**
     * Query images from Androids local MediaStore (`DCIM/` and `Pictures/`)
     */
    @RequiresPermission(
        allOf = [
            // Android 9 or lower
            Manifest.permission.READ_EXTERNAL_STORAGE,

            // access images > Android 9
            Manifest.permission.READ_MEDIA_IMAGES,

            // access videos > Android 9
            Manifest.permission.READ_MEDIA_VIDEO,

            // access any geographic locations > Android 9
            Manifest.permission.ACCESS_MEDIA_LOCATION,
        ],
    )
    @Suppress("ForbiddenComment", "NestedBlockDepth", "LongMethod", "CyclomaticComplexMethod")
    override fun queryLocalImages(): List<MediaItem> {
        val photos = mutableListOf<MediaItem>()

        val selection = null
        val selectionArgs = null
        val sortOrder = "${android.provider.MediaStore.Images.Media.DATE_TAKEN} DESC"

        val applicationContext = application.applicationContext

        applicationContext.contentResolver.query(
            generateContentUri(),
            generateProjection(),
            selection,
            selectionArgs,
            sortOrder,
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.SIZE)
            val dateTakenColumn = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATE_TAKEN)

            val latColumn = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.LATITUDE)
            } else {
                -1
            }
            val longColumn = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.LONGITUDE)
            } else {
                -1
            }

            val fnumberColumn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.F_NUMBER)
            } else {
                -1
            }

            val isoNumberColumn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.ISO)
            } else {
                -1
            }

            val exposureColumn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.EXPOSURE_TIME)
            } else {
                -1
            }

            logcat(LogPriority.ERROR) { "count: ${cursor.count}" }

            while (cursor.moveToNext()) {
                var photoUri = Uri.withAppendedPath(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    cursor.getString(idColumn),
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
                    logcat(LogPriority.WARN) { "Implement ACCESS_MEDIA_LOCATION permission for exif location" }
                    try {
                        photoUri = android.provider.MediaStore.setRequireOriginal(photoUri)
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
                    } catch (exception: UnsupportedOperationException) {
                        logcat(LogPriority.WARN) { "ACCESS_MEDIA_LOCATION permission not granted for exif location" }
                        logcat(LogPriority.DEBUG) { exception.asLog() }
                    }
                } else {
                    if (latColumn != -1 && longColumn != -1) {
                        latLong = floatArrayOf(
                            cursor.getFloat(latColumn),
                            cursor.getFloat(longColumn),
                        )
                    }
                }

                logcat(priority = LogPriority.ERROR) {
                    "details: exposure=$exposure, " +
                        "fnumber=$fnumber, " +
                        "isoNumber=$isoNumber, " +
                        "lat=${latLong[0]}, " +
                        "lon=${latLong[0]}"
                }

                photos += MediaItem(
                    id = id,
                    name = name,
                    size = size,
                    uri = photoUri,
                    dateTaken = Instant.ofEpochMilli(dateTaken),
                    exposure = exposure,
                    fnumber = fnumber,
                    isoNumber = isoNumber,
                    location = Location(latLong[0], latLong[1], 0),
                )
            }
        }

        return photos
    }

    /**
     * Query videos from Androids local MediaStore (`DCIM/`, `Movies/`, and `Pictures/`)
     */
    override fun queryLocalVideos(): List<MediaItem> {
        TODO("Not yet implemented")
    }

    private fun generateContentUri(): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            android.provider.MediaStore.Images.Media.getContentUri(
                android.provider.MediaStore.VOLUME_EXTERNAL,
            )
        } else {
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
    }

    private fun generateProjection(): Array<String> {
        val projection = mutableListOf<String>()

        projection += android.provider.MediaStore.Images.Media._ID
        projection += android.provider.MediaStore.Images.Media.DISPLAY_NAME
        projection += android.provider.MediaStore.Images.Media.SIZE
        projection += android.provider.MediaStore.Images.Media.DATE_TAKEN

        // deprecated with 29 (Q)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            projection += android.provider.MediaStore.Images.Media.LATITUDE
            projection += android.provider.MediaStore.Images.Media.LONGITUDE
        }

        // added with 30 (R)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            projection += android.provider.MediaStore.Images.Media.F_NUMBER
            projection += android.provider.MediaStore.Images.Media.ISO
            projection += android.provider.MediaStore.Images.Media.EXPOSURE_TIME
        }

        return projection.toTypedArray()
    }
}
