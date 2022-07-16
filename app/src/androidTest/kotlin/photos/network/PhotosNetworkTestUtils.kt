package photos.network

import java.time.Instant
import photos.network.data.photos.repository.Photo

fun generateTestPhoto(
    filename: String,
): Photo {
    return Photo(
        filename = filename,
        imageUrl = filename,
        dateAdded = Instant.now(),
        dateTaken = null,
        dateModified = null,
        isPrivate = false,
        uri = null,
    )
}
