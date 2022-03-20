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
package photos.network.domain

import org.koin.dsl.module
import photos.network.domain.photos.usecase.GetPhotoUseCase
import photos.network.domain.photos.usecase.GetPhotosUseCase
import photos.network.domain.settings.usecase.GetPrivacyStateUseCase
import photos.network.domain.settings.usecase.TogglePrivacyStateUseCase

val domainModule = module {
    factory {
        GetPhotosUseCase(
            photoRepository = get(),
            settingsRepository = get(),
        )
    }

    factory {
        GetPhotoUseCase(
            photoRepository = get()
        )
    }

    factory {
        GetPrivacyStateUseCase(
            settingsRepository = get()
        )
    }

    factory {
        TogglePrivacyStateUseCase(
            settingsRepository = get()
        )
    }
}
