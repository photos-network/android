package photos.network.domain.settings.usecase

import photos.network.data.user.repository.UserRepository

class VerifyServerHostUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(host: String): Boolean {
        return if (host.length > 8) {
            userRepository.verifyServerHost(host)
        } else {
            false
        }
    }
}
