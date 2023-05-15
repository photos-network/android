/*
 * Copyright 2020-2023 Photos.network developers
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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import photos.network.common.persistence.PrivacyState
import photos.network.domain.settings.usecase.GetSettingsUseCase
import photos.network.domain.settings.usecase.TogglePrivacyUseCase

class HomeViewModel constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val togglePrivacyStateUseCase: TogglePrivacyUseCase,
) : ViewModel() {
    val uiState = MutableStateFlow(HomeUiState())

    init {
        loadInitialPrivacyState()
    }

    private fun loadInitialPrivacyState() {
        viewModelScope.launch(Dispatchers.IO) {
            getSettingsUseCase().collect { settings ->
                withContext(Dispatchers.Main) {
                    uiState.update {
                        it.copy(isPrivacyEnabled = settings.privacyState == PrivacyState.ACTIVE)
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
