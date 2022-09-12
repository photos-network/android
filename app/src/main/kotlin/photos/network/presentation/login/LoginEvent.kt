package photos.network.presentation.login

sealed class LoginEvent {
    class VerifyAuthCode(val authCode: String) : LoginEvent()
    object UserCancelledLogin : LoginEvent()
    object VerificationFailed : LoginEvent()
}
