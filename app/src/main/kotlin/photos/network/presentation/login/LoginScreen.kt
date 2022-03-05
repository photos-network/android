/*
 * Copyright 2020-2022 Photos.network developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package photos.network.presentation.login

import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.viewModel
import photos.network.navigation.Destination

/**
 * app screen to enter user credentials to authenticate
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    val viewModel: LoginViewModel by viewModel()

    LoginScreen(
        modifier = modifier,
        host = viewModel.host,
        clientId = viewModel.clientId,
        clientSecret = viewModel.clientSecret,
        onAuthCode = { authCode: String ->
            viewModel.requestAccessToken(authCode) { success ->
                if (success) {
                    Log.i("Login", "Successfully loaded an access token.")
                    navController.navigate(route = Destination.Home.route) {
                        launchSingleTop = true
                        popUpTo(Destination.Home.route) {
                            inclusive = true
                        }
                    }
                } else {
                    // TODO: show user facing error message
                    Log.e("Login", "Request access token failed")
                }
            }
        },
        onError = { error: String ->
            Log.e("Login", error)
            // TODO: show user facing error message
        }
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    host: MutableState<String>,
    clientId: MutableState<String>,
    clientSecret: MutableState<String>,
    onAuthCode: (authCode: String) -> Unit,
    onError: (error: String) -> Unit,
) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // generate random nonce for EACH request to prevent replay attacs
        val nonce = remember {
            val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            var tmpNonce = ""
            for (i in 1..12) {
                tmpNonce += chars.random()
            }
            tmpNonce
        }

        AndroidView(
            factory = { viewBlockContext ->
                WebView(viewBlockContext)
            },
            modifier = Modifier.fillMaxSize()
        ) { webView ->
            val redirectUri = "photosapp://authenticate"

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
                loadUrl("${host.value}/api/oauth/authorize?client_id=${clientId.value}&response_type=code&redirect_uri=$redirectUri&response_mode=query&scope=openid profile email phone library%3Awrite&response_type=code&response_mode=query&state=$nonce")
            }
        }
    }
}
