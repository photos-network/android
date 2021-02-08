package network.photos.android.app.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import network.photos.android.app.composables.AppTheme
import network.photos.android.composables.LoginScreen
import network.photos.android.oauth.OAuthViewModel

/**
 * show oauth login via webview
 */
class LoginFragment : Fragment() {
    private val viewModel: OAuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        setContent {
            AppTheme {
                LoginScreen(
                    host = viewModel.host,
                    clientId = viewModel.clientId,
                    clientSecret = viewModel.clientSecret
                )
            }
        }
    }
}
