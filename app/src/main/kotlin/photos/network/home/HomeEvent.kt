package photos.network.home

sealed class HomeEvent {
    object LogoutEvent: HomeEvent()
    object LoginEvent: HomeEvent()
    object TogglePrivacyEvent: HomeEvent()
}
