package photos.network.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import org.koin.androidx.compose.viewModel
import photos.network.domain.user.User

val LocalCurrentUser = staticCompositionLocalOf<User?> {
    error("LocalCurrentUser not provided")
}

/**
 * Inject the current user into lower composables in the composition tree if logged-in.
 */
@Composable
fun CurrentUserHost(
    content: @Composable () -> Unit
) {
    val viewModel: CurrentUserViewModel by viewModel()

    CompositionLocalProvider(LocalCurrentUser provides viewModel.currentUser) {
        content()
    }
}
