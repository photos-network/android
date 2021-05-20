package network.photos.android.data.user.network.model

import com.squareup.moshi.Json

data class UserResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "email") val email: String,
    @field:Json(name = "lastname") val lastname: String,
    @field:Json(name = "firstname") val firstname: String,
    @field:Json(name = "lastSeen") val lastSeen: String,
)
