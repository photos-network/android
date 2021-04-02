package network.photos.android.remote.data

data class PhotoList(
    val limit: Int,
    val offset: Int,
    val size: Int,
    val results: List<SimplePhotoElement>,
)
