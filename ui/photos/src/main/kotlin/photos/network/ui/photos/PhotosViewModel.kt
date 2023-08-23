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
package photos.network.ui.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import logcat.LogPriority
import logcat.logcat
import photos.network.common.persistence.PrivacyState
import photos.network.domain.photos.usecase.GetFacesUseCase
import photos.network.domain.photos.usecase.GetPhotosUseCase
import photos.network.domain.photos.usecase.StartPhotosSyncUseCase
import photos.network.domain.settings.usecase.GetSettingsUseCase
import photos.network.domain.settings.usecase.TogglePrivacyUseCase

class PhotosViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val togglePrivacyStateUseCase: TogglePrivacyUseCase,
    private val getPhotosUseCase: GetPhotosUseCase,
    private val startPhotosSyncUseCase: StartPhotosSyncUseCase,
    private val getFacesUseCase: GetFacesUseCase,
) : ViewModel() {
    val uiState = MutableStateFlow(PhotosUiState())

    init {
        loadInitialPrivacyState()

        viewModelScope.launch(Dispatchers.IO) {
            loadPhotos()
        }
    }

    fun handleEvent(event: PhotosEvent) {
        when (event) {
            PhotosEvent.StartLocalPhotoSyncEvent -> startLocalPhotoSync()
            is PhotosEvent.SelectIndex -> selectItem(event.index)
            PhotosEvent.SelectPreviousPhoto -> selectPreviousPhoto()
            PhotosEvent.SelectNextPhoto -> selectNextPhoto()
            PhotosEvent.TogglePrivacyEvent -> {
                viewModelScope.launch(Dispatchers.IO) {
                    togglePrivacyStateUseCase()
                }
            }
            PhotosEvent.ToggleFaces -> {
                viewModelScope.launch(Dispatchers.IO) {
                    uiState.update { it.copy(showFaces = !it.showFaces) }
                }
            }
        }
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

    internal suspend fun loadPhotos() {
        getPhotosUseCase().collect { photos ->
            withContext(Dispatchers.Main) {
                uiState.update {
                    it.copy(
                        photos = photos,
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun startLocalPhotoSync() {
        viewModelScope.launch(Dispatchers.IO) {
            startPhotosSyncUseCase()
        }
    }

    private fun selectPreviousPhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value.selectedIndex?.let { index ->
                val newIndex = index - 1
                if (newIndex >= 0) {
                    val photo = uiState.value.photos[newIndex]

                    withContext(Dispatchers.Main) {
                        uiState.update {
                            it.copy(
                                selectedPhoto = photo,
                                selectedIndex = newIndex,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun selectNextPhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value.selectedIndex?.let { index ->
                val newIndex = index + 1
                if (newIndex < uiState.value.photos.size) {
                    val photo = uiState.value.photos[newIndex]

                    withContext(Dispatchers.Main) {
                        uiState.update {
                            it.copy(
                                selectedPhoto = photo,
                                selectedIndex = newIndex,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun selectItem(index: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            val photo = if (index != null) {
                uiState.value.photos[index]
            } else {
                null
            }

            logcat(LogPriority.ERROR) { "uri:${photo?.uri}" }

            photo?.uri?.let { uri ->
                getFacesUseCase(uri).collect { boxes ->
                    logcat(LogPriority.ERROR) { "boxes:${boxes.size}" }
                    withContext(Dispatchers.Main) {
                        uiState.update {
                            it.copy(faces = boxes)
                        }
                    }
                }
            }
            withContext(Dispatchers.Main) {
                uiState.update {
                    it.copy(
                        selectedPhoto = photo,
                        selectedIndex = index,
                    )
                }
            }
        }
    }
}
