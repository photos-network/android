package photos.network.domain.settings.usecase

import photos.network.data.settings.repository.SettingsRepository

class UpdateHostUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(host: String): Unit {
        settingsRepository.updateHost(host)
    }
}
