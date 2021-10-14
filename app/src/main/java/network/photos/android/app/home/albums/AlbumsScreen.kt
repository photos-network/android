package network.photos.android.app.home.albums

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberAnimatedNavController(),
) {
    Text(text = "Albums")
}
