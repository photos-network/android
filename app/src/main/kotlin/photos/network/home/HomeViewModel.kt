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
package photos.network.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import logcat.LogPriority
import logcat.logcat
import photos.network.data.Resource
import photos.network.data.photos.entities.Photo
import photos.network.domain.photos.usecase.GetPhotosUseCase
import photos.network.domain.photos.usecase.StartPhotosSyncUseCase
import photos.network.domain.settings.Settings
import photos.network.domain.user.User
import photos.network.domain.user.usecase.GetCurrentUserUseCase
import photos.network.domain.user.usecase.LogoutUseCase
import photos.network.user.LocalCurrentUser

class HomeViewModel constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val startPhotosSyncUseCase: StartPhotosSyncUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(loading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        logcat(LogPriority.ERROR) { "init HomeVM" }
        refreshPhotos()
    }

    fun refreshPhotos() {
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            getPhotosUseCase { photos ->
                photos.collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _uiState.update { it.copy(photos = result.data!!, loading = false) }
                        }
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    errorMessages = result.message,
                                    loading = false
                                )
                            }
                        }
                        is Resource.Loading -> {
                            _uiState.update { it.copy(loading = true) }
                        }
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase.run()
        }
    }
}

data class HomeUiState(
    val photos: List<Photo> = emptyList(),
    val loading: Boolean = false,
    val refreshError: Boolean = false,
    val errorMessages: String? = null,
)
