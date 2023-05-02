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
package photos.network.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import org.koin.androidx.compose.viewModel
import photos.network.repository.sharing.User

val LocalCurrentUser = staticCompositionLocalOf<User?> {
    error("LocalCurrentUser not provided")
}

/**
 * Inject the current user into lower composables in the composition tree if logged-in.
 */
@Composable
fun CurrentUserHost(
    content: @Composable () -> Unit,
) {
    val viewModel: CurrentUserViewModel by viewModel()

    CompositionLocalProvider(LocalCurrentUser provides viewModel.currentUser) {
        content()
    }
}
