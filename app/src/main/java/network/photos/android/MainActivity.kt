package network.photos.android

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import network.photos.android.app.composables.AppTheme
import network.photos.android.app.home.HomeViewModel
import network.photos.android.app.home.photos.PhotosViewModel
import network.photos.android.app.onboarding.login.LoginViewModel
import network.photos.android.app.onboarding.setup.SetupViewModel
import network.photos.android.composables.GridScreen
import network.photos.android.composables.LoginScreen
import network.photos.android.composables.SetupScreen

/**
 * Main entry point, showing a splash screen on first start, followed by a setup progress.
 * On subsequent starts it will redirect to the photos grid.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            AppTheme {
                NavHost(
                    navController = navController,
                    startDestination = "home",
                ) {
                    composable(route = "setup") {
                        val setupViewModel = hiltViewModel<SetupViewModel>()

                        SetupScreen(
                            host = setupViewModel.host,
                            clientId = setupViewModel.clientId,
                            clientSecret = setupViewModel.clientSecret,
                            isConnectionCheckInProgress = setupViewModel.isConnectionCheckInProgress,
                            isConnectionValid = setupViewModel.isConnectionValid,
                            onNextClick = {
                                setupViewModel.checkConnection(onSuccess = {
                                    navController.navigate("login")
                                })
                            },
                            onHelpClick = {
                                Log.e("TAG", "Not implemented yet.")
                            })
                    }
                    composable(route = "login") {
                        val viewModel = hiltViewModel<LoginViewModel>()
                        LoginScreen(
                            host = viewModel.host,
                            clientId = viewModel.clientId,
                            clientSecret = viewModel.clientSecret,
                            onAuthCode = { authCode: String ->
                                viewModel.requestAccessToken(authCode) { success ->
                                    if (success) {
                                        navController.navigate("home")
                                    } else {
                                        // TODO: show user facing error message
                                        Log.e("LoginFrgmnt", "Request access token failed")
                                    }
                                }
                            },
                            onError = { error: String ->
                                Log.e("OAuth", error)
                                // TODO: show user facing error message
                            }
                        )
                    }
                    composable(route = "home") {
                        val homeViewModel = hiltViewModel<HomeViewModel>()
                        val gridViewModel = hiltViewModel<PhotosViewModel>()

                        if (homeViewModel.currentSettings.value == null) {
                            navController.navigate("setup")
                            return@composable
                        }

                        if (homeViewModel.currentUser.value == null) {
                            navController.navigate("login")
                            return@composable
                        }

                        // TODO: add bottom navigation (photos / albums)
                        GridScreen(
                            photos = gridViewModel.photos,
                            requestPhotos = {
                                gridViewModel.getPhotos()
                            }
                        )
                    }
                    composable(route = "photo/{id}", arguments = listOf(navArgument("id") {
                        type = NavType.StringType
                    })) {
                        // TODO: add detail screen here
                        Text("Add detail screen for photo $id")
                    }
                }
            }
        }
    }
}
