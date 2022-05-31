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
package photos.network.presentation.login

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import logcat.LogPriority
import logcat.logcat
import photos.network.domain.settings.usecase.GetSettingsUseCase
import photos.network.domain.user.usecase.RequestAccessTokenUseCase

class LoginViewModel(
    private val requestAccessTokenUseCase: RequestAccessTokenUseCase,
    private val settingsUseCase: GetSettingsUseCase,
) : ViewModel() {
    val uiState = mutableStateOf(LoginUiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            generateRandomNonce()
        }

        viewModelScope.launch(Dispatchers.IO) {
            settingsUseCase().collect {
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(
                        host = it.host,
                        clientId = it.clientId
                    )
                }
            }
        }
    }

    fun handleEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.UserCancelledLogin -> {
                logcat(LogPriority.WARN) { "User cancelled login" }
            }
            LoginEvent.VerificationFailed -> {
                logcat(LogPriority.WARN) { "Verification failed" }
            }
            is LoginEvent.VerifyAuthCode -> requestAccessToken(event.authCode)
        }
    }

    /**
     * generate random nonce for EACH request to prevent replay attacs
     */
    private fun generateRandomNonce() {
        viewModelScope.launch(Dispatchers.IO) {
            val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            var tmpNonce = ""
            for (i in 1..12) {
                tmpNonce += chars.random()
            }

            withContext(Dispatchers.Main) {
                uiState.value = uiState.value.copy(
                    nonce = tmpNonce
                )
            }
        }
    }

    private fun requestAccessToken(authCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (requestAccessTokenUseCase(authCode)) {
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(
                        loginSucceded = true
                    )
                }
            }
        }
    }
}
