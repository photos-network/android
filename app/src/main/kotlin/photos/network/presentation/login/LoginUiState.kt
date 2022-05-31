package photos.network.presentation.login

data class LoginUiState(
    val loginSucceded: Boolean = false,
    val nonce: String = "",
    val host: String = "",
    val clientId: String = "",
)
