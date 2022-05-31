package photos.network.domain.user.usecase

import photos.network.data.user.repository.UserRepository

class RequestAccessTokenUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(authCode: String): Boolean {
        return userRepository.accessTokenRequest(authCode)
    }
}
