/*
 * Copyright 2020-2022 Photos.network developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package photos.network.presentation.setup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import logcat.logcat
import photos.network.data.settings.persistence.entities.SettingsDto
import photos.network.data.settings.repository.SettingsRepository
import photos.network.data.user.repository.UserRepository

class SetupViewModel(
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SetupUiState(loading = true))
    val uiState: StateFlow<SetupUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    fun setHost(host: String) {
        _uiState.update { it.copy(host = host) }
    }

    fun setClientId(clientId: String) {
        _uiState.update { it.copy(clientId = clientId) }
    }

    fun setClientSecret(clientSecret: String) {
        _uiState.update { it.copy(clientSecret = clientSecret) }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.loadSettings()?.let { settings ->
                _uiState.update {
                    it.copy(
                        host = settings.host,
                        clientId = settings.clientId,
                        clientSecret = settings.clientSecret,
                    )
                }
            }

            _uiState.update {
                it.copy(
                    currentUser = userRepository.currentUser()
                )
            }
        }
    }

    fun checkConnection(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isConnectionCheckInProgress = true
                )
            }

            if (uiState.value.host == null || uiState.value.clientId == null || uiState.value.clientSecret == null) {
                Log.e("SetupVM", "loaded settings incomplete!")
                _uiState.update {
                    it.copy(
                        isConnectionValid = false
                    )
                }
            } else {
                // TODO: try to connect to host
                Log.e("SetupVM", "save settings. Host: ${uiState.value.host}")
                settingsRepository.saveSettings(
                    SettingsDto(
                        host = uiState.value.host!!,
                        redirectUri = "",
                        authCode = "",
                        clientId = uiState.value.clientId!!,
                        clientSecret = uiState.value.clientSecret!!,
                        scope = "",
                    )
                )
                logcat { "new Host: ${settingsRepository.host}" }

                val user = userRepository.currentUser()
                Log.e("SetupVM", "currentUser: '$user'")
                // TODO: if connection was successfull
                _uiState.update {
                    it.copy(
                        isConnectionValid = true,
                        isConnectionCheckInProgress = false
                    )
                }

                onSuccess()
            }
        }
    }
}
