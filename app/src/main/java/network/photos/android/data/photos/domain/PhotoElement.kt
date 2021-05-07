package network.photos.android.data.photos.domain

data class PhotoElement(
    val id: String,
    val name: String,
    val owner: String?,
    val created_at: String?,
    val modified_at: String?,
    val details: TechnicalDetails?,
    val tags: List<String>?,
    val location: Location?,
    val image_url: String
)
