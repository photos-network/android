package network.photos.android.app.onboarding.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import network.photos.android.app.R
import network.photos.android.app.composables.AppTheme
import network.photos.android.composables.SetupScreen

/**
 * Setup the connection
 */
@AndroidEntryPoint
class SetupFragment : Fragment() {
    private val viewModel: SetupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        setContent {
            AppTheme {
                if (viewModel.currentUser.value != null) {
                    findNavController().navigate(R.id.nav_grid)
                } else {
                    if (viewModel.isConnectionValid.value) {
                        findNavController().navigate(R.id.nav_login)
                    } else {
                        SetupScreen(
                            host = viewModel.host,
                            clientId = viewModel.clientId,
                            clientSecret = viewModel.clientSecret,
                            isConnectionCheckInProgress = viewModel.isConnectionCheckInProgress,
                            isConnectionValid = viewModel.isConnectionValid,
                            onButtonClick = { viewModel.checkConnection() },
                            onHelpClick = {
                                // TODO: show help
                                // TODO: navigate when connection could be established
                                findNavController().navigate(R.id.nav_login)
                            }
                        )
                    }
                }
            }
        }
    }
}
