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

import photos.network.repository.photos.Photo
import photos.network.repository.photos.model.Box

data class PhotosUiState(
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    val hasImagePermission: Boolean = false,
    val hasLocationPermission: Boolean = false,
    val isPrivacyEnabled: Boolean = false,
    val photos: List<Photo> = emptyList(),
    val selectedPhoto: Photo? = null,
    val selectedIndex: Int? = null,
    val showFaces: Boolean = false,
    val faces: List<Box> = emptyList()
)
