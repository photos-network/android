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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import photos.network.data.settings.persistence.SettingsStorage
import photos.network.data.settings.persistence.Settings as PersistenceSettings

class SettingsRepositoryImpl(
    private val settingsStore: SettingsStorage,
) : SettingsRepository {
    private var currentSettings: PersistenceSettings? = null

    override val settings: Flow<Settings> = flow {
        while (true) {
            loadSettings()

            currentSettings?.let { dto ->
                val privacyState = PrivacyState.valueOf(dto.privacyState)
                emit(
                    Settings(
                        host = dto.host ?: "",
                        clientId = dto.clientId ?: "",
                        privacyState = privacyState,
                    )
                )
            }
            delay(POLL_INTERVAL)
        }
    }

    init {
        loadSettings()
    }

    override suspend fun updateSettings(newSettings: Settings) {
        withContext(Dispatchers.IO) {
            currentSettings = PersistenceSettings(
                host = newSettings.host,
                clientId = newSettings.clientId,
                privacyState = newSettings.privacyState.toString()
            )
            saveSettings()
        }
    }

    override suspend fun deleteSettings() {
        withContext(Dispatchers.IO) {
            currentSettings = null
            settingsStore.delete()
        }
    }

    override suspend fun togglePrivacy() {
        currentSettings?.let {
            val newValue = if (it.privacyState == PrivacyState.NONE.toString()) {
                PrivacyState.ACTIVE.toString()
            } else {
                PrivacyState.NONE.toString()
            }
            val new = PersistenceSettings(
                host = it.host,
                clientId = it.clientId,
                privacyState = newValue,
            )
            currentSettings = new

            saveSettings()
        }
    }

    override suspend fun updateHost(newHost: String) {
        withContext(Dispatchers.IO) {
            currentSettings?.let {
                val new = PersistenceSettings(
                    host = newHost,
                    clientId = it.clientId,
                    privacyState = it.privacyState,
                )
                currentSettings = new

                saveSettings()
            }
        }
    }

    override suspend fun updateClientId(newClientId: String) {
        withContext(Dispatchers.IO) {
            currentSettings?.let {
                val new = PersistenceSettings(
                    host = it.host,
                    clientId = newClientId,
                    privacyState = it.privacyState,
                )
                currentSettings = new

                saveSettings()
            }
        }
    }

    internal fun loadSettings() {
        if (currentSettings == null) {
            currentSettings = settingsStore.read().takeIf {
                it != null
            }
        }

        if (currentSettings == null) {
            currentSettings = PersistenceSettings()
        }
    }

    internal fun saveSettings() {
        currentSettings?.let {
            settingsStore.save(it)
        }
    }

    companion object {
        private const val POLL_INTERVAL = 500L
    }
}
