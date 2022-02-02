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
package photos.network.presentation.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import logcat.LogPriority
import logcat.logcat
import photos.network.data.settings.repository.SettingsRepository
import photos.network.data.user.repository.UserRepository

class LoginViewModel(
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val host = mutableStateOf("https://photos.stuermer.pro")
    val clientId = mutableStateOf("1803463f-c10f-4a65-aa15-b2e39be9f14d")
    val clientSecret = mutableStateOf("1TmlFYywRd7MwlbRNiePjQ")

    init {
        viewModelScope.launch {
            val settings = settingsRepository.loadSettings()
            settings?.let {
                logcat(LogPriority.WARN) { "update LoginVM from loaded settings." }
                host.value = settings.host
                clientId.value = settings.clientId
                clientSecret.value = settings.clientSecret
            }
        }
    }

    fun requestAccessToken(authCode: String, callback: (success: Boolean) -> Unit) {
        // TODO: request auth code
        logcat(LogPriority.ERROR) { "TODO request access token with authCode: $authCode" }
        viewModelScope.launch {
            callback(userRepository.requestAuthorization(authCode, clientId.value))
        }
    }
}
