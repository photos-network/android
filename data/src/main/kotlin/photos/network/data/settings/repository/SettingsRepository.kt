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
package photos.network.data.settings.repository

import photos.network.data.settings.persistence.entities.SettingsDto

interface SettingsRepository {
    val authCode: String?
    val clientId: String?
    val clientSecret: String?
    val redirectUri: String?
    val scope: String?
    val host: String?

    fun loadSettings(): SettingsDto?

    fun saveSettings(settings: SettingsDto)
}
