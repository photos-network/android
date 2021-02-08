package network.photos.android.composables

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import network.photos.android.app.composables.AppTypography

@Preview(name = "Login")
@Composable
fun PreviewLoginScreen() {
    val host: MutableState<String> = mutableStateOf("")
    val clientId: MutableState<String> = mutableStateOf("")
    val clientSecret: MutableState<String> = mutableStateOf("")

    LoginScreen(
        host = host,
        clientId = clientId,
        clientSecret = clientSecret,
    )
}

@Composable
fun LoginScreen(
    host: MutableState<String>,
    clientId: MutableState<String>,
    clientSecret: MutableState<String>
) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            viewBlock = ::WebView,
            modifier = Modifier.fillMaxSize()
        ) { webView ->
            with(webView) {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                loadUrl("${host.value}/oauth/authorize?client_id=${clientId.value}&redirect_uri=photosapp:///authenticate&scope=openid profile email phone library%3Awrite&response_type=code&response_mode=query&nonce=uogotry1uji")
            }
        }
    }
}
