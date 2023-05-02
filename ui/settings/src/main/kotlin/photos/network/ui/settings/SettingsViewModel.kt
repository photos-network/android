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
package photos.network.ui.settings

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import photos.network.domain.settings.usecase.GetSettingsUseCase
import photos.network.domain.settings.usecase.UpdateClientIdUseCase
import photos.network.domain.settings.usecase.UpdateHostUseCase
import photos.network.domain.settings.usecase.VerifyClientIdUseCase
import photos.network.domain.settings.usecase.VerifyServerHostUseCase

class SettingsViewModel(
    private val application: Application,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateHostUseCase: UpdateHostUseCase,
    private val updateClientIdUseCase: UpdateClientIdUseCase,
    private val verifyServerHostUseCase: VerifyServerHostUseCase,
    private val verifyClientIdUseCase: VerifyClientIdUseCase,
) : ViewModel() {
    val uiState = MutableStateFlow(SettingsUiState())
    private val isHostVerified = mutableStateOf(false)
    private val isClientIdVerified = mutableStateOf(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getSettingsUseCase().collect { settings ->
                withContext(Dispatchers.Main) {
                    uiState.update {
                        it.copy(
                            host = settings.host ?: "",
                            isHostVerified = isHostVerified.value,
                            clientId = settings.clientId ?: "",
                            isClientVerified = isClientIdVerified.value,
                            // TDOO: get VERSION_NAME and VERSION_CODE
//                            appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                            appVersion = "{BuildConfig.VERSION_NAME} ({BuildConfig.VERSION_CODE})",
                        )
                    }
                }
            }
        }
    }

    fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.HostChanged -> updateHost(event.newHost)
            is SettingsEvent.ClientIdChanged -> updateClientId(event.newId)
            SettingsEvent.ToggleServerSetup -> toggleServerSetup()
            SettingsEvent.SetClipboardEvent -> copyIntoClipboard()
        }
    }

    private fun toggleServerSetup() {
        uiState.update {
            it.copy(
                isServerSetupExpanded = !uiState.value.isServerSetupExpanded,
            )
        }
    }

    private fun copyIntoClipboard() {
        val context = application.applicationContext
        val clipboard: ClipboardManager? =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?

        // TODO: get VERSION_NAME
        val clip = ClipData.newPlainText("Photos.networdk", "BuildConfig.VERSION_NAME")
        clipboard?.setPrimaryClip(clip)
    }

    private fun updateHost(host: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateHostUseCase(host = host)
            isHostVerified.value = verifyServerHostUseCase(host)
        }
    }

    private fun updateClientId(clientId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateClientIdUseCase(clientId = clientId)
            isClientIdVerified.value = verifyClientIdUseCase(clientId)
        }
    }
}
