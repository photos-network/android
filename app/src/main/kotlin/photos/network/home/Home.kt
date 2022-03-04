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
package photos.network.home

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import org.koin.androidx.compose.getViewModel
import photos.network.account.AccountScreen
import photos.network.details.DetailScreen
import photos.network.home.photos.PhotosScreen
import photos.network.navigation.Destination
import photos.network.presentation.help.HelpScreen
import photos.network.presentation.login.LoginScreen
import photos.network.presentation.setup.SetupScreen
import photos.network.theme.AppTheme
import photos.network.home.albums.AlbumsScreen
import photos.network.home.folders.FoldersScreen
import photos.network.ui.UserAvatar

/**
 * Default app screen containing a searchbar, photos grid, albums tab and more.
 */
@Composable
fun Home(
    modifier: Modifier = Modifier,
    orientation: Int
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination by derivedStateOf {
        Destination.fromString(navBackStackEntry.value?.destination?.route)
    }
//    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scrollState: LazyListState = rememberLazyListState()

    val viewmodel: HomeViewModel = getViewModel()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("HomeScreenTag"),
//        scaffoldState = scaffoldState,
        snackbarHost = {
//            SnackbarHost(
//                hostState = it,
//                modifier = Modifier.systemBarsPadding()
//            )
        },
        topBar = {
            MediumTopAppBar(
                modifier = Modifier.padding(top = 36.dp),
                //elevation = if (scrollState.firstVisibleItemIndex < 0 || scrollState.firstVisibleItemScrollOffset < 0) 0.dp else 4.dp,
                title = {
                    UserAvatar(
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Destination.Account.route)
                            }
                            .size(32.dp),
                        user = null
                    )
                },
                actions = {
//                    IconButton(
//                        onClick = {
//                            // TODO: show notifications
//                        }
//                    ) {
//                        Icon(Icons.Outlined.Search, null)
//                    }
                    IconButton(
                        onClick = {
                            viewmodel.handleEvent(HomeEvent.TogglePrivacyEvent)
                        }
                    ) {
                        if (viewmodel.uiState.value.isPrivacyEnabled) {
                            Icon(Icons.Outlined.Shield, null)
                        } else {
                            Icon(Icons.Default.Shield, null)
                        }
                    }
//                    IconButton(
//                        onClick = {
//                            // TODO: show SettingsScreen
//                        }
//                    ) {
//                        Icon(Icons.Outlined.MoreVert, null)
//                    }
                },
//                backgroundColor = MaterialTheme.colors.surface,
            )
        },
        bottomBar = {
            if (currentDestination.isRootDestination()) {
                NavigationBar(modifier = Modifier.padding(bottom = 48.dp)) {
                    // Photos
                    NavigationBarItem(
                        icon = { Icon(Destination.Photos.icon, contentDescription = null) },
                        label = { Text(stringResource(Destination.Photos.resourceId)) },
                        selected = currentDestination == Destination.Photos,
                        onClick = {
                            navController.navigate(Destination.Photos.route)
                        }
                    )

                    // Albums
                    NavigationBarItem(
                        icon = { Icon(Destination.Albums.icon, contentDescription = null) },
                        label = { Text(stringResource(Destination.Albums.resourceId)) },
                        selected = currentDestination == Destination.Albums,
                        onClick = {
                            navController.navigate(Destination.Albums.route)
                        }
                    )

                    // Folders
                    NavigationBarItem(
                        icon = { Icon(Destination.Albums.icon, contentDescription = null) },
                        label = { Text(stringResource(Destination.Folders.resourceId)) },
                        selected = currentDestination == Destination.Folders,
                        onClick = {
                            navController.navigate(Destination.Folders.route)
                        }
                    )
                }
            }
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination = Destination.Photos.route,
                ) {
                    composable(route = Destination.Photos.route) { PhotosScreen(navController = navController) }
                    composable(route = Destination.Albums.route) { AlbumsScreen(navController = navController) }
                    composable(route = Destination.Folders.route) { FoldersScreen(navController = navController) }
                    composable(route = Destination.Account.route) { AccountScreen(navController = navController) }
                    composable(route = Destination.Setup.route) { SetupScreen(navController = navController) }
                    composable(route = Destination.Login.route) { LoginScreen(navController = navController) }
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
    )
}

@Preview(name = "Home")
@Preview(name = "Home · DARK", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeScreen() {
    AppTheme {
        Home(
            modifier = Modifier.fillMaxSize(),
            orientation = Configuration.ORIENTATION_LANDSCAPE
        )
    }
}
