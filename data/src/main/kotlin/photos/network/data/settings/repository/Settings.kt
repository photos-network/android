package photos.network.data.settings.repository

data class Settings(
    val host: String = "",
    val port: Int = 443,
    val clientId: String = "",
    val privacyState: PrivacyState = PrivacyState.NONE,
)
