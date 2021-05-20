package network.photos.android.app.onboarding.setup

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import network.photos.android.data.settings.domain.Settings
import network.photos.android.data.settings.repository.SettingsRepository
import network.photos.android.data.user.domain.User
import network.photos.android.data.user.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val currentUser = mutableStateOf<User?>(null)

    val host = mutableStateOf<String?>(null)
    val clientId = mutableStateOf<String?>(null)
    val clientSecret = mutableStateOf<String?>(null)

    private val _isConnectionCheckInProgress = mutableStateOf(false)
    val isConnectionCheckInProgress: MutableState<Boolean>
        get() = _isConnectionCheckInProgress

    private val _isConnectionValid = mutableStateOf(false)
    val isConnectionValid: MutableState<Boolean>
        get() = _isConnectionValid

    init {
        viewModelScope.launch {
            settingsRepository.loadSettings()?.let { settings ->
                host.value = settings.host
                clientId.value = settings.clientId
                clientSecret.value = settings.clientSecret
            }

            currentUser.value = userRepository.currentUser()
        }
    }

    fun checkConnection(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isConnectionCheckInProgress.value = true

            // TODO: remove delay
            delay(3_000)

            if (host.value == null || clientId.value == null || clientSecret.value == null) {
                isConnectionValid.value = false
            } else {
                // TODO: try to connect to host
                Log.e("SetupVM", "save settings. Host: ${host.value}")
                settingsRepository.saveSettings(
                    Settings(
                        host = host.value!!,
                        clientId = clientId.value!!,
                        clientSecret = clientSecret.value!!
                    )
                )
                // TODO: if connection was successfull
                _isConnectionValid.value = true
                _isConnectionCheckInProgress.value = false

                onSuccess()
            }
        }
    }
}
