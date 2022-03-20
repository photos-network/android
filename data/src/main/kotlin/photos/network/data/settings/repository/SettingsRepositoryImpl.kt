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
package photos.network.data.settings.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import photos.network.data.settings.persistence.SettingsStorage
import photos.network.data.settings.persistence.entities.SettingsDto

class SettingsRepositoryImpl(
    private val settingsStore: SettingsStorage
) : SettingsRepository {
    private var currentSettings: SettingsDto? = null
    override var privacyState: Flow<PrivacyState> = flow {
        while (true) {
            if (currentSettings == null) {
                currentSettings = settingsStore.read().takeIf {
                    it != null
                }
            }

            emit(
                PrivacyState.values().firstOrNull {
                    it.value == currentSettings?.privacyState
                } ?: PrivacyState.NONE
            )
            delay(POLL_INTERVAL)
        }
    }

    override val authCode: String?
        get() = currentSettings?.authCode
    override val clientId: String?
        get() = currentSettings?.clientId
    override val clientSecret: String?
        get() = currentSettings?.clientSecret
    override val redirectUri: String?
        get() = currentSettings?.redirectUri
    override val scope: String?
        get() = currentSettings?.scope
    override val host: String?
        get() = currentSettings?.host

    companion object {
        private const val POLL_INTERVAL = 10_000L
    }

    override fun togglePrivacy() {
        // TODO: flip state
    }

    override fun loadSettings(): SettingsDto? {
        if (currentSettings != null) {
            return currentSettings
        }

        currentSettings = settingsStore.read().takeIf {
            it != null
        }

        return currentSettings
    }

    override fun saveSettings(settings: SettingsDto) {
        currentSettings = settings
        settingsStore.save(settings)
    }
}
