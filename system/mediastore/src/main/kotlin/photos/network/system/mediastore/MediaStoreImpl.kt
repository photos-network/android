package photos.network.system.mediastore

import android.app.Application
import android.location.Location
import android.net.Uri
import android.os.Build
import java.time.Instant
import logcat.LogPriority
import logcat.logcat

class MediaStoreImpl(
    private val application: Application
): MediaStore {
    /**
     * Query images from Androids local MediaStore (`DCIM/` and `Pictures/`)
     */
    override fun queryLocalMediaStore(): List<MediaItem> {
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
                    // TODO: ACCESS_MEDIA_LOCATION permission required
                    logcat(LogPriority.WARN) { "Implement ACCESS_MEDIA_LOCATION permission for exif location" }
//                    photoUri = MediaStore.setRequireOriginal(photoUri)
//                    val stream: InputStream? =
//                        applicationContext.contentResolver.openInputStream(photoUri)
//                    if (stream == null) {
//                        logcat(LogPriority.WARN) { "Got a null input stream for $photoUri" }
//                        continue
//                    }
//
//                    val exifInterface = ExifInterface(stream)
//                    // If it returns null, fall back to {0.0, 0.0}.
//                    exifInterface.getLatLong(latLong)
//
//                    stream.close()
                } else {
                    if (latColumn != -1 && longColumn != -1) {
                        latLong = floatArrayOf(
                            cursor.getFloat(latColumn),
                            cursor.getFloat(longColumn),
                        )
                    }
                }

                logcat(priority = LogPriority.ERROR) { "details: exposure=$exposure, fnumber=$fnumber, isoNumber=$isoNumber, lat=${latLong[0]}, lon=${latLong[0]}" }

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
