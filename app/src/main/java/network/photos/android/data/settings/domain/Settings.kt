package network.photos.android.data.settings.domain

class Settings(
    val host: String,
    val clientId: String,
    val clientSecret: String,
    val useSSL: Boolean = false
)
