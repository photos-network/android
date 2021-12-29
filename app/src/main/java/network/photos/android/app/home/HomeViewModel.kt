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
package network.photos.android.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import network.photos.android.data.Resource
import network.photos.android.data.photos.domain.PhotoElement
import network.photos.android.data.photos.repository.PhotoRepository
import network.photos.android.data.settings.domain.Settings
import network.photos.android.data.settings.repository.SettingsRepository
import network.photos.android.data.user.domain.User
import network.photos.android.data.user.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository,
    private val photoRepository: PhotoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(loading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadSettings()
        loadUser()
    }

    fun loadSettings() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    currentSettings = settingsRepository.loadSettings()
                )
            }
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    currentUser = userRepository.currentUser(),
                    loading = false,
                )
            }
        }
    }

    fun refreshPhotos() {
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            val result = photoRepository.getPhotos(
                token = userRepository.currentUser()?.token,
                page = 0
            )
            _uiState.update {
                when (result) {
                    is Resource.Success -> it.copy(photos = result.data!!, loading = false)
                    is Resource.Error -> {
                        it.copy(errorMessages = result.message, loading = false)
                    }
                    is Resource.Loading -> {
                        it.copy(loading = true)
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    currentUser = null,
                )
            }
            userRepository.invalidateAuthorization()
        }
    }
}

data class HomeUiState(
    val photos: List<PhotoElement> = emptyList(),
    val currentUser: User? = null,
    val currentSettings: Settings? = null,
    val loading: Boolean = false,
    val refreshError: Boolean = false,
    val errorMessages: String? = null,
)
