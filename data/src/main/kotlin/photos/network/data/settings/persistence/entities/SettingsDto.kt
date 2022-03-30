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
package photos.network.data.settings.persistence.entities

import kotlinx.serialization.Serializable
import photos.network.data.settings.repository.PrivacyState

@Serializable
class SettingsDto(
    val host: String? = null,
    val redirectUri: String? = null,
    val authCode: String? = null,
    val clientId: String? = null,
    val clientSecret: String? = null,
    val scope: String? = null,
    val useSSL: Boolean = false,
    val privacyState: String = PrivacyState.NONE.value,
)
