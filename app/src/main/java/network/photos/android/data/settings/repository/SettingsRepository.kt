package network.photos.android.data.settings.repository

import network.photos.android.data.settings.domain.Settings

interface SettingsRepository {
    suspend fun loadSettings(): Settings?

    suspend fun saveSettings(settings: Settings)
}
