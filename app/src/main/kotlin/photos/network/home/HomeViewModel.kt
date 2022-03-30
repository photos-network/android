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
package photos.network.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import photos.network.data.settings.repository.PrivacyState
import photos.network.domain.settings.usecase.GetPrivacyStateUseCase
import photos.network.domain.settings.usecase.TogglePrivacyStateUseCase

class HomeViewModel constructor(
    private val getPrivacyStateUseCase: GetPrivacyStateUseCase,
    private val togglePrivacyStateUseCase: TogglePrivacyStateUseCase,
) : ViewModel() {
    val uiState = mutableStateOf(HomeUiState())

    init {
        loadInitialPrivacyState()
    }

    private fun loadInitialPrivacyState() {
        viewModelScope.launch(Dispatchers.IO) {
            getPrivacyStateUseCase().collect { privacyState ->
                withContext(Dispatchers.Main) {
                    if (privacyState == PrivacyState.ACTIVE) {
                        uiState.value = uiState.value.copy(isPrivacyEnabled = true)
                    } else {
                        uiState.value = uiState.value.copy(isPrivacyEnabled = false)
                    }
                }
            }
        }
    }

    fun handleEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.TogglePrivacyEvent -> {
                viewModelScope.launch(Dispatchers.IO) {
                    togglePrivacyStateUseCase()
                }
            }
        }
    }
}
