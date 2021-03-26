package network.photos.android.composables

import android.net.Uri
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
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
        {},
        {}
    )
}

@Composable
fun LoginScreen(
    host: MutableState<String>,
    clientId: MutableState<String>,
    clientSecret: MutableState<String>,
    onAuthCode: (authCode: String) -> Unit,
    onError: (error: String) -> Unit,
) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView<WebView>(
            factory = { viewBlockContext ->
                WebView(viewBlockContext)
            },
            modifier = Modifier.fillMaxSize()
        ) { webView ->
            val redirectUri =  "photosapp://authenticate"
            val nonce = "uogotry1uji"

            with(webView) {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        request?.let {
                            // check oauth redirect
                            if (request.url.toString().startsWith(redirectUri)) {
                                // check the 'state' to prevent CSRF attacks. Ignore if not matching with sent 'nonce'
                                val responseState = request.url.getQueryParameter("state")
                                if (responseState == nonce) {
                                    request.url.getQueryParameter("code")?.let { authCode ->
                                        onAuthCode.invoke(authCode)
                                    } ?: run {
                                        onError.invoke("User cancelled the login flow!")
                                    }
                                } else {
                                    onError.invoke("Could not verify 'state'")
                                }
                            }
                        }
                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }
                webChromeClient = WebChromeClient()
                loadUrl("${host.value}/oauth/authorize?client_id=${clientId.value}&redirect_uri=${redirectUri}&scope=openid profile email phone library%3Awrite&response_type=code&response_mode=query&state=${nonce}")
            }
        }
    }
}
