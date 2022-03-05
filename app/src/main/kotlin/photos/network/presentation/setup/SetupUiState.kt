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
package photos.network.presentation.setup

import photos.network.data.user.persistence.entities.DatabaseUser

data class SetupUiState(
    val currentUser: DatabaseUser? = null,
    val host: String? = "https://photos.stuermer.pro",
    val clientId: String? = "1803463f-c10f-4a65-aa15-b2e39be9f14d",
    val clientSecret: String? = "1TmlFYywRd7MwlbRNiePjQ",
    val isConnectionCheckInProgress: Boolean = false,
    val isConnectionValid: Boolean = false,
    val loading: Boolean = false,
    val errorMessage: String? = null
)
