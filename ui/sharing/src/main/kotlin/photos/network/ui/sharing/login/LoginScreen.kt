/*
 * Copyright 2020-2023 Photos.network developers
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
package photos.network.ui.sharing.login

import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import logcat.LogPriority
import logcat.logcat
import org.koin.androidx.compose.viewModel
import photos.network.ui.common.navigation.Destination

/**
 * app screen to enter user credentials to authenticate
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    host: String = "",
    client: String = "",
) {
    val viewmodel: LoginViewModel by viewModel()
    viewmodel.sethost(host)
    viewmodel.setclient(client)

    LoginScreen(
        modifier = modifier,
        uiState = viewmodel.uiState.collectAsState().value,
        handleEvent = viewmodel::handleEvent,
        navigateToHome = {
            navController.navigate(route = Destination.Photos.route) {
                launchSingleTop = true
                popUpTo(Destination.Photos.route) {
                    inclusive = true
                }
            }
        },
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    handleEvent: (event: LoginEvent) -> Unit,
    navigateToHome: () -> Unit = {},
) {
    if (uiState.loginSucceded) {
        navigateToHome()
    }
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AndroidView(
            factory = { viewBlockContext ->
                WebView(viewBlockContext)
            },
            modifier = Modifier.fillMaxSize(),
        ) { webView ->
            val redirectUri = "photosapp://authenticate"

            with(webView) {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?,
                    ): Boolean {
                        request?.let {
                            logcat(LogPriority.ERROR) { "url=$redirectUri" }
                            logcat(LogPriority.ERROR) { "url=${request.url}" }
                            // check oauth redirect
                            if (request.url.toString().startsWith(redirectUri)) {
                                // check the 'state' to prevent CSRF attacks. Ignore if not matching with sent 'nonce'
                                val responseState = request.url.getQueryParameter("state")

                                if (responseState == uiState.nonce) {
                                    request.url.getQueryParameter("code")?.let { authCode ->
                                        handleEvent(LoginEvent.VerifyAuthCode(authCode))
                                    } ?: run {
                                        handleEvent(LoginEvent.UserCancelledLogin)
                                    }
                                } else {
                                    handleEvent(LoginEvent.VerificationFailed)
                                }
                            }
                        }
                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }
                webChromeClient = WebChromeClient()
                loadUrl("${uiState.host}/api/oauth/authorize?client_id=${uiState.clientId}&response_type=code&redirect_uri=$redirectUri&response_mode=query&scope=openid profile email phone library%3Awrite&response_type=code&response_mode=query&state=${uiState.nonce}")
            }
        }
    }
}
