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
package network.photos.android.data.settings.repository

import network.photos.android.data.settings.domain.Settings
import network.photos.android.data.settings.storage.SettingsStorage

class SettingsRepositoryImpl(
    private val settingsStore: SettingsStorage
) : SettingsRepository {
    private var currentSettings: Settings? = null

    override fun loadSettings(): Settings? {
        if (currentSettings != null) {
            return currentSettings
        }

        currentSettings = settingsStore.readSettings().takeIf {
            it != null
        }

        return currentSettings
    }

    override fun saveSettings(settings: Settings) {
        settingsStore.writeSettings(settings)
    }
}
