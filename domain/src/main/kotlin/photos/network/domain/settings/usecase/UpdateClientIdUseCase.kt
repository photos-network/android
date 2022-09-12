package photos.network.domain.settings.usecase

import photos.network.data.settings.repository.SettingsRepository

class UpdateClientIdUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(clientId: String) {
        settingsRepository.updateClientId(clientId)
    }
}
