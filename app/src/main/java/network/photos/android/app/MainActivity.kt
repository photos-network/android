package network.photos.android.app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import network.photos.android.composables.app.AppScaffold
import network.photos.android.app.databinding.ActivityMainBinding

/**
 * Main entry point, showing a splash screen on first start, followed by a setup progress.
 * On subsequent starts it will redirect to the photos grid.
 */
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val scaffoldState = rememberScaffoldState()

            AppScaffold(
                scaffoldState
            ) {
                // Inflate the XML layout using View Binding:
                AndroidViewBinding(ActivityMainBinding::inflate)
            }
        }
    }
}
