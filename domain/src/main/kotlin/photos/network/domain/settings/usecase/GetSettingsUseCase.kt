package photos.network.domain.settings.usecase

import kotlinx.coroutines.flow.Flow
import photos.network.data.settings.repository.Settings
import photos.network.data.settings.repository.SettingsRepository

class GetSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Settings> {
        return settingsRepository.settings
    }
}
