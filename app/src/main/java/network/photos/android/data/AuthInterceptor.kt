package network.photos.android.data

import network.photos.android.data.user.repository.UserRepository
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AuthInterceptor(
    private val userRepository: UserRepository
) : Authenticator {
    /**
     * add an authToken to each request and refresh when HTTP401 is received.
     */
    override fun authenticate(route: Route?, response: Response): Request? {
        return userRepository.currentUser()?.let { user ->
            response.request.newBuilder()
                .addHeader("Authorization", user.token?.accessToken ?: "")
                .build()
        }
    }
}
