package photos.network.data.user.network.entity

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class VerifyClientIdResponse(
    val error: String,

    @SerialName("error_description")
    val errorDescription: String
)
