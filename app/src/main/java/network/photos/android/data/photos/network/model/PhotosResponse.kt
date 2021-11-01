package network.photos.android.data.photos.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotosResponse(
    @SerialName("offset") val offset: Int,
    @SerialName("limit") val limit: Int,
    @SerialName("size") val size: Int,
    @SerialName("results") val results: List<PhotoItem>,
)

@Serializable
data class PhotoItem(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("date_added") val dateAdded: String? = null,
    @SerialName("date_taken") val dateTaken: String? = null,
)
