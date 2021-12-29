/*
 * Copyright 2020-2021 Photos.network developers
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
package network.photos.android.app.onboarding.setup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import network.photos.android.data.settings.domain.Settings
import network.photos.android.data.settings.repository.SettingsRepository
import network.photos.android.data.user.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
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
                    Settings(
                        host = uiState.value.host!!,
                        clientId = uiState.value.clientId!!,
                        clientSecret = uiState.value.clientSecret!!
                    )
                )
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
