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
package photos.network.domain.photos.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import photos.network.common.persistence.PrivacyState
import photos.network.repository.photos.Photo
import photos.network.repository.photos.PhotoRepository
import photos.network.repository.settings.SettingsRepository

/**
 * Load a list of photos, filtered based on the users privacy filter.
 */
class GetPhotosUseCase(
    private val photoRepository: PhotoRepository,
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(): Flow<List<Photo>> {
        return combine(
            photoRepository.getPhotos(),
            settingsRepository.settings,
        ) { photos, settings ->
            if (settings.privacyState == PrivacyState.ACTIVE) {
                photos.filterNot {
                    it.isPrivate
                }
            } else {
                photos
            }
        }
    }
}
