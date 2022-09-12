package photos.network

import photos.network.data.photos.repository.Photo
import java.time.Instant

fun generateTestPhoto(
    filename: String,
): Photo {
    return Photo(
        filename = filename,
        imageUrl = "https://via.placeholder.com/300x300/6FA4DE/010041?text=$filename",
        dateAdded = Instant.now(),
        dateTaken = null,
        dateModified = null,
        isPrivate = false,
        uri = null,
    )
}
