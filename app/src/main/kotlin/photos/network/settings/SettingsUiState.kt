package photos.network.settings

data class SettingsUiState(
    val serverStatus: ServerStatus = ServerStatus.UNAVAILABLE,
)

enum class ServerStatus {
    AVAILABLE(),
    UNAVAILABLE(),
    PROGRESS(),
    UNAUTHORIZED(),
}
