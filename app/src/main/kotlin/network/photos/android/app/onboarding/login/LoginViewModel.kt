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

    fun requestAccessToken(authCode: String, callback: (success: Boolean) -> Unit) {
        viewModelScope.launch {
            callback.invoke(userRepository.requestAuthorization(authCode, clientId.value))
        }
    }
}
