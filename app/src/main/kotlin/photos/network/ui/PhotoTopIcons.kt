package photos.network.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import photos.network.theme.AppTheme

@Composable
fun PhotoTopIcons(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
) {
    Row(modifier = modifier) {
        IconButton(
            onClick = { onBackPressed() }
        ) {
            Icon(Icons.Filled.ArrowBack, null)
        }
    }
}

@Preview
@Composable
private fun PhotoTopIconPreview() {
    AppTheme {
        PhotoTopIcons()
    }
}
