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
package photos.network.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import photos.network.domain.photos.usecase.GetPhotoUseCase

class DetailViewModel(
    private val getPhotoUseCase: GetPhotoUseCase
) : ViewModel() {
    val uiState = mutableStateOf(DetailUiState())

    fun handleEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.SetIdentifier -> {
                setIdentifier(event.identifier)
            }
            is DetailEvent.Delete -> {}
            is DetailEvent.Share -> {}
        }
    }

    private fun setIdentifier(identifier: String) {
        uiState.value = uiState.value.copy(photoIdentifier = identifier)
        viewModelScope.launch {
            getPhotoUseCase(identifier).collect { photo ->
                uiState.value = uiState.value.copy(
                    uri = photo?.uri,
                    imageUrl = photo?.imageUrl,
                    isLoading = false
                )
            }
        }
    }
}
