package network.photos.android.data.user.repository

import network.photos.android.data.user.domain.User

interface UserRepository {
    fun currentUser(): User?

    suspend fun requestAuthorization(authCode: String, clientId: String): Boolean
}
