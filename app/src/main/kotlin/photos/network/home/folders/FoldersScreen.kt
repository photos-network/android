package photos.network.home.folders

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import photos.network.home.albums.AlbumsContent
import photos.network.navigation.Destination
import photos.network.theme.AppTheme

@Composable
fun FoldersScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    FoldersContent()
}

@Composable
fun FoldersContent(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Destination.Folders.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Coming soon",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = "Here you'll be able to browse folders on this device",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(
    name = "Folders",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Folders · DARK",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun PreviewAlbumContent() {
    AppTheme {
        FoldersContent()
    }
}
