package network.photos.android.data.user.repository

import android.util.Log
import network.photos.android.data.user.domain.Token
import network.photos.android.data.user.domain.User
import network.photos.android.data.user.network.UserApi
import network.photos.android.data.user.storage.UserStorage
import java.util.*

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val userStorage: UserStorage
) : UserRepository {
    private var currentUser: User? = null

    override fun currentUser(): User? {
        if (currentUser != null) return currentUser

        userStorage.read()?.let {
            currentUser = it
            return it
        }

        return null
    }

    override suspend fun requestAuthorization(authCode: String, clientId: String): Boolean {
        val tokenResponse =
            userApi.accessTokenRequest(authCode, clientId, "http://127.0.0.1:3000/callback")
        Log.i("UserRepo", "accessToken: ${tokenResponse.accessToken}")

        val userDetails = userApi.me("Bearer ${tokenResponse.accessToken}")

        val user = User(
            id = UUID.randomUUID(),
            lastname = userDetails.lastname,
            firstname = userDetails.firstname,
            token = Token(
                accessToken = tokenResponse.accessToken,
                refreshToken = tokenResponse.refreshToken
            )
        )
        currentUser = user
        userStorage.save(user)

        return true
    }
}
