package network.photos.android.data.user.network.model

import com.squareup.moshi.Json

data class TokenResponse(
    @field:Json(name = "access_token") val accessToken: String,
    @field:Json(name = "token_type") val tokenType: String? = null,
    @field:Json(name = "expires_in") val expiresIn: Long,
    @field:Json(name = "refresh_token") val refreshToken: String
)
