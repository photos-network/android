package photos.network.account

data class AccountUiState(
    val serverStatus: ServerStatus = ServerStatus.UNAVAILABLE,
)

enum class ServerStatus {
    AVAILABLE(),
    UNAVAILABLE(),
    PROGRESS(),
}
