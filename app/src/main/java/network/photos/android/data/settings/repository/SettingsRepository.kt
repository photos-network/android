package network.photos.android.data.settings.repository

import network.photos.android.data.settings.domain.Settings

interface SettingsRepository {
    fun loadSettings(): Settings?

    fun saveSettings(settings: Settings)
}
