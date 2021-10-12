package network.photos.android.app.onboarding.setup

import network.photos.android.data.user.domain.User

data class SetupUiState(
    val currentUser: User? = null,
    val host: String? = "https://slack.stuermer.pro",
    val clientId: String? = "1803463f-c10f-4a65-aa15-b2e39be9f14d",
    val clientSecret: String? = "1TmlFYywRd7MwlbRNiePjQ",
    val isConnectionCheckInProgress: Boolean = false,
    val isConnectionValid: Boolean = false,
    val loading: Boolean = false,
    val errorMessage: String? = null
)
