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
package photos.network

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import logcat.logcat
import photos.network.home.Home
import photos.network.ui.common.theme.AppTheme
import photos.network.user.CurrentUserHost

/**
 * Main entry point, handling navigation events.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (!BuildConfig.DEBUG) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE,
            )
        }

        setContent {
            PhotosApp(
                windowSizeClass = calculateWindowSizeClass(this),
                displayFeatures = calculateDisplayFeatures(this),
            )
        }
    }

    /**
     * Handle specific configuration changes to prevent activity recreation in the Manifest.
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks whether a keyboard is available
        if (newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
            logcat { "Keyboard available" }
        } else if (newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_NO) {
            logcat { "No Keyboard" }
        }
    }
}

val LocalAppVersion = staticCompositionLocalOf { "Unknown" }

@Composable
fun PhotosApp(
    systemUiController: SystemUiController = rememberSystemUiController(),
    windowSizeClass: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
) {
    val useDarkIcons = !isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
        )
        systemUiController.isStatusBarVisible = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            systemUiController.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            systemUiController.systemBarsBehavior = BEHAVIOR_SHOW_BARS_BY_SWIPE
        }
    }

    CompositionLocalProvider(LocalAppVersion provides BuildConfig.VERSION_NAME) {
        AppTheme {
            CurrentUserHost {
                Home(
                    modifier = Modifier.fillMaxSize(),
                    orientation = LocalConfiguration.current.orientation,
                    windowSizeClass = windowSizeClass,
                    displayFeatures = displayFeatures,
                )
            }
        }
    }
}
