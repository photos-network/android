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
package network.photos.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import network.photos.android.app.details.DetailScreen
import network.photos.android.app.help.HelpScreen
import network.photos.android.app.home.HomeScreen
import network.photos.android.app.onboarding.login.LoginScreen
import network.photos.android.app.onboarding.setup.SetupScreen
import network.photos.android.navigation.Destination
import network.photos.android.theme.AppTheme
import network.photos.android.theme.darkColors
import network.photos.android.theme.lightColors

/**
 * Main entry point, showing a splash screen on first start, followed by a setup progress.
 * On subsequent starts it will redirect to the photos grid.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PhotosApp(startDestination = Destination.Home.route)
        }
    }
}

@Composable
fun PhotosApp(
    startDestination: String = Destination.Home.route,
) {
    val navController = rememberAnimatedNavController()
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
    }

    AppTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
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
