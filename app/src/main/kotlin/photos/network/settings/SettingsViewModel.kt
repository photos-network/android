package photos.network.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {
    val uiState = mutableStateOf(SettingsUiState())

    fun handleEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.ForceSync -> {}
            SettingsEvent.EditProfile -> {}
            is SettingsEvent.HostChanged -> updateHost(event.newHost)
            is SettingsEvent.ClientIdChanged -> updateHost(event.newId)
            is SettingsEvent.ClientSecretChanged -> updateHost(event.newSecret)
            SettingsEvent.Login -> {}
            SettingsEvent.ToggleActivityLog -> {}
        }
    }

    private fun updateHost(host: String) {
        uiState.value = uiState.value.copy(
            host = host
        )
    }

    private fun updateClientId(clientId: String) {
        uiState.value = uiState.value.copy(
            clientId = clientId
        )
    }
    private fun updateClientSecret(clientSecret: String) {
        uiState.value = uiState.value.copy(
            clientSecret = clientSecret
        )
    }
}
