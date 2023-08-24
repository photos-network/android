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
package photos.network.domain.photos.usecase

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import photos.network.repository.photos.PhotoRepository
import photos.network.repository.photos.model.Box

class GetFacesUseCase(
    private val photoRepository: PhotoRepository,
) {
    operator fun invoke(photoUri: Uri): Flow<List<Box>> {
        return photoRepository.getFaces(photoUri = photoUri)
    }
}
