package network.photos.android.data.photos.domain

data class PhotoList(
    val limit: Int,
    val offset: Int,
    val size: Int,
    val results: List<PhotoElement>,
)
