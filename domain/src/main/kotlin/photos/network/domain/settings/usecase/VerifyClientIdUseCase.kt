package photos.network.domain.settings.usecase

import photos.network.data.user.repository.UserRepository

class VerifyClientIdUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(clientId: String): Boolean {
        return if (clientId.length > 10) {
            userRepository.verifyClientId(clientId)
        } else {
            false
        }
    }
}
