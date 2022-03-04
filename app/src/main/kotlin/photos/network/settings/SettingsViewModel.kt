package photos.network.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {
    val uiState = mutableStateOf(SettingsUiState())

    fun handleEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.ForceSync -> forceSync()
        }
    }

    private fun forceSync() {

    }
}
