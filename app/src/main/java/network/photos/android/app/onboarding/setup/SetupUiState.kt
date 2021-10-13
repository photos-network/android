package network.photos.android.app.onboarding.setup

import network.photos.android.data.user.domain.User

data class SetupUiState(
    val currentUser: User? = null,
    val host: String? = null,
    val clientId: String? = null,
    val clientSecret: String? = null,
    val isConnectionCheckInProgress: Boolean = false,
    val isConnectionValid: Boolean = false,
    val loading: Boolean = false,
    val errorMessage: String? = null
)
