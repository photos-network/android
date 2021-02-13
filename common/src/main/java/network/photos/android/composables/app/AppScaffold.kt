package network.photos.android.composables.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import network.photos.android.app.composables.AppTheme

@Composable
fun AppScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    content: @Composable (PaddingValues) -> Unit
) {
    AppTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            bodyContent = content
        )
    }
}
