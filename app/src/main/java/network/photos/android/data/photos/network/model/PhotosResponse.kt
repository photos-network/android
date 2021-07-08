package network.photos.android.data.photos.network.model

import com.squareup.moshi.Json

data class PhotosResponse(
    @field:Json(name = "offset") val offset: Int,
    @field:Json(name = "limit") val limit: Int,
    @field:Json(name = "size") val size: Int,
    @field:Json(name = "results") val results: List<PhotoItem>,
)

data class PhotoItem(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "image_url") val imageUrl: String,
)
