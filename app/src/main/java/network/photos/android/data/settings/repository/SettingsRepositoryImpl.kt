package network.photos.android.data.settings.repository

import network.photos.android.data.settings.domain.Settings
import network.photos.android.data.settings.storage.SettingsStorage

class SettingsRepositoryImpl(
    private val settingsStore: SettingsStorage
): SettingsRepository {
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
