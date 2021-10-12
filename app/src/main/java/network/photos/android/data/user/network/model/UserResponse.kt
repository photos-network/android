package network.photos.android.data.user.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("lastname") val lastname: String,
    @SerialName("firstname") val firstname: String,
    @SerialName("lastSeen") val lastSeen: String,
)
