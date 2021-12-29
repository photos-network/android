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
package network.photos.android.app.onboarding.setup

import network.photos.android.data.user.domain.User

data class SetupUiState(
    val currentUser: User? = null,
    val host: String? = null,
    val clientId: String? = null,
    val clientSecret: String? = null,
    val isConnectionCheckInProgress: Boolean = false,
    val isConnectionValid: Boolean = false,
    val loading: Boolean = false,
    val errorMessage: String? = null
)
