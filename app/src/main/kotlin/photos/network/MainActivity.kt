/*
 * Copyright 2020-2021 Photos.network developers
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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import photos.network.navigation.Destination
import photos.network.details.DetailScreen
import photos.network.presentation.help.HelpScreen
import photos.network.home.HomeScreen
import photos.network.presentation.login.LoginScreen
import photos.network.presentation.setup.SetupScreen
import photos.network.theme.AppTheme
import photos.network.theme.darkColors
import photos.network.theme.lightColors
import photos.network.user.CurrentUserHost

/**
 * Main entry point, handling navigation events.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            PhotosApp()
        }
    }
}

val LocalAppVersion = staticCompositionLocalOf { "Unknown" }

@Composable
fun PhotosApp(
    startDestination: String = Destination.Home.route,
    navController: NavHostController = rememberAnimatedNavController(),
    systemUiController: SystemUiController = rememberSystemUiController(),
) {
    val useDarkIcons = !isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
    }

    CompositionLocalProvider(LocalAppVersion provides BuildConfig.VERSION_NAME) {
        AppTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
            CurrentUserHost {
                AnimatedNavHost(
                    navController = navController,
                    startDestination = startDestination,
                ) {
                    composable(route = Destination.Setup.route) { SetupScreen(navController = navController) }
                    composable(route = Destination.Login.route) { LoginScreen(navController = navController) }
                    composable(route = Destination.Home.route) { HomeScreen(navController = navController) }
                    composable(route = Destination.Help.route) { HelpScreen(navController = navController) }
                    composable(
                        route = "${Destination.Details.route}/{identifier}",
                        arguments = listOf(
                            navArgument("identifier") {
                                defaultValue = "-1"
                                type = NavType.StringType
                            }
                        )
                    ) { backStackEntry ->
                        backStackEntry.arguments?.getString("identifier")?.let {
                            DetailScreen(
                                navController = navController,
                                photoIdentifier = it
                            )
                        }
                    }
                }
            }
        }
    }
}
