package network.photos.android.app.onboarding.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import network.photos.android.app.R
import network.photos.android.app.composables.AppTheme
import network.photos.android.composables.LoginScreen

/**
 * show oauth login via webview
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {

        setContent {
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            AppTheme {
                LoginScreen(
                    host = viewModel.host,
                    clientId = viewModel.clientId,
                    clientSecret = viewModel.clientSecret,
                    { authCode: String ->
                        viewModel.requestAccessToken(authCode) {
                            if (it) {
                                findNavController().navigate(R.id.nav_grid)
                            } else {
                                // TODO: show error message
                                Log.e("LoginFrgmnt", "Request access token failed")
                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("Error: Request access token failed")
                                }
                            }
                        }
                    },
                    { error: String ->
                        Log.e("OAuth", error)
                        // TODO: show user facing error
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Error: $error")
                        }
                    }
                )
            }
        }
    }
}
