package network.photos.android.app.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import network.photos.android.app.R
import network.photos.android.app.composables.AppTheme

class SplashFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        setContent {
            AppTheme {
                SplashScreen(onTimeout = {
                    findNavController().navigate(R.id.nav_grid)
                })
            }
        }
    }
}
