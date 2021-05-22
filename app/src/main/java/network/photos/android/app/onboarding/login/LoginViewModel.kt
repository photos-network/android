package network.photos.android.app.onboarding.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import network.photos.android.data.settings.repository.SettingsRepository
import network.photos.android.data.user.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val host = mutableStateOf("")
    val clientId = mutableStateOf("")
    val clientSecret = mutableStateOf("")

    init {
        viewModelScope.launch {
            val settings = settingsRepository.loadSettings()
            settings?.let {
                host.value = settings.host
                clientId.value = settings.clientId
                clientSecret.value = settings.clientSecret
            }
        }
    }

    fun requestAccessToken(authCode: String, callback : (success: Boolean) -> Unit) {
        viewModelScope.launch {
            callback.invoke(userRepository.requestAuthorization(authCode, clientId.value))
        }
    }
}
