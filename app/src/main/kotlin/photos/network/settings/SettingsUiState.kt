package photos.network.settings

data class SettingsUiState(
    val serverStatus: ServerStatus = ServerStatus.UNAVAILABLE,
    val host: String = "",
    val clientId: String = "",
    val clientSecret: String = "",
)

enum class ServerStatus {
    AVAILABLE(),
    UNAVAILABLE(),
    PROGRESS(),
    UNAUTHORIZED(),
}
