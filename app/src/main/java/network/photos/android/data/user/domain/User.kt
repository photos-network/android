package network.photos.android.data.user.domain

import java.util.UUID

class User(
    val id: UUID,
    val lastname: String,
    val firstname: String,
    val token: Token
)
