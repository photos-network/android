package network.photos.android.data.settings.repository

import network.photos.android.data.settings.domain.Settings
import network.photos.android.data.settings.storage.SettingsStorage

class SettingsRepositoryImpl(
    private val settingsStore: SettingsStorage
): SettingsRepository {
    private val currentSettings: Settings? = null

    override suspend fun loadSettings(): Settings? {
        if (currentSettings != null) return currentSettings

        return settingsStore.readSettings()?.takeIf { it.host.isNotEmpty() }
    }

    override suspend fun saveSettings(settings: Settings) {
        settingsStore.writeSettings(settings)
    }
}
