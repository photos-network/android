package photos.network.data.user.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val message: String,
)
