package photos.network.system.mediastore

import android.net.Uri
import java.time.Instant

interface MediaStore {
    fun queryLocalMediaStore(): List<MediaItem>
}

data class MediaItem(
    val id: Long,
    val name: String,
    val size: Int,
    val uri: Uri,
    val dateTaken: Instant? = null,
    val exposure: String? = null,
    val fnumber: String? = null,
    val isoNumber: String? = null,
    val location: Location? = null
)

data class Location(
    val latitude: Float,
    val longitude: Float,
    val altitude: Int
)
