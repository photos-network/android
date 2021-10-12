package network.photos.android.data.user.domain

import java.util.UUID

class User(
    val id: UUID = UUID.randomUUID(),
    val lastname: String = "",
    val firstname: String = "",
    val profileImageUrl: String = "",
    val token: Token? = null,
)
