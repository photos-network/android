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
package photos.network.home.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import photos.network.domain.photos.usecase.GetPhotosUseCase
import photos.network.domain.photos.usecase.StartPhotosSyncUseCase

class PhotosViewModel(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val startPhotosSyncUseCase: StartPhotosSyncUseCase,
) : ViewModel() {
    val uiState = MutableStateFlow(PhotosUiState())

    init {
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
        }
    }

    internal suspend fun loadPhotos() {
        getPhotosUseCase().collect { photos ->
            withContext(Dispatchers.Main) {
                uiState.update {
                    it.copy(
                        photos = photos,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun startLocalPhotoSync() {
        startPhotosSyncUseCase()
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
                                selectedIndex = newIndex
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
                                selectedIndex = newIndex
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
            withContext(Dispatchers.Main) {
                uiState.update {
                    it.copy(
                        selectedPhoto = photo,
                        selectedIndex = index
                    )
                }
            }
        }
    }
}
