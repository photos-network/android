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
package photos.network

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import photos.network.details.DetailViewModel
import photos.network.domain.photos.usecase.StartPhotosSyncUseCase
import photos.network.domain.user.usecase.LogoutUseCase
import photos.network.home.HomeViewModel
import photos.network.home.photos.PhotosViewModel
import photos.network.presentation.login.LoginViewModel
import photos.network.presentation.setup.SetupViewModel
import photos.network.settings.SettingsViewModel
import photos.network.user.CurrentUserViewModel

val appModule = module {
    viewModel {
        CurrentUserViewModel(
            userRepository = get()
        )
    }
    viewModel {
        DetailViewModel(
            getPhotoUseCase = get()
        )
    }
    viewModel {
        PhotosViewModel(
            getPhotosUseCase = get(),
            startPhotosSyncUseCase = StartPhotosSyncUseCase(
                photoRepository = get()
            ),
        )
    }
    viewModel {
        LoginViewModel(
            settingsRepository = get(),
            userRepository = get()
        )
    }
    viewModel {
        SetupViewModel(
            settingsRepository = get(),
            userRepository = get()
        )
    }
    viewModel {
        HomeViewModel(
            logoutUseCase = LogoutUseCase(
                userRepository = get()
            ),
        )
    }
    viewModel {
        SettingsViewModel()
    }
}
