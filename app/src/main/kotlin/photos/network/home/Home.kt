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
package photos.network.home

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import org.koin.androidx.compose.getViewModel
import photos.network.R
import photos.network.api.ServerStatus
import photos.network.ui.common.components.AppLogo
import photos.network.ui.common.navigation.Destination
import photos.network.ui.common.theme.AppTheme

/**
 * Default app screen containing a searchbar, photos grid, albums tab and more.
 */
@Composable
fun Home(
    modifier: Modifier = Modifier,
    orientation: Int,
    windowSizeClass: WindowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1920.dp, 1080.dp)),
    displayFeatures: List<DisplayFeature> = listOf(),
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination by remember {
        derivedStateOf {
            Destination.fromString(navBackStackEntry.value?.destination?.route)
        }
    }

    val viewmodel: HomeViewModel = getViewModel()

    val showTopBar = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
    val showBottomNavigation = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
    val showNavigationRail = !showBottomNavigation

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("HomeScreenTag"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentDestination.isRootDestination()) {
                // privacy
                TopAppBar(
                    title = {},
                    modifier = Modifier,
                    navigationIcon = {
                        AppLogo(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    navController.navigate(Destination.Account.route)
                                },
                            size = 32.dp,
                            statusSize = 16.dp,
                            serverStatus = ServerStatus.UNAVAILABLE,
                        )
                    },
                    actions = {
                        // privacy
                        if (currentDestination == Destination.Photos || currentDestination == Destination.Albums) {
                            IconButton(
                                onClick = {
                                    viewmodel.handleEvent(HomeEvent.TogglePrivacyEvent)
                                },
                            ) {
                                if (viewmodel.uiState.collectAsState().value.isPrivacyEnabled) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = stringResource(id = R.string.privacy_filter_enabled_description),
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.Shield,
                                        contentDescription = stringResource(id = R.string.privacy_filter_disabled_description),
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            }
        },
        bottomBar = {
            if (showBottomNavigation && currentDestination.isRootDestination()) {
                HomeBottomNavigation(
                    currentDestination = currentDestination,
                    navController = navController,
                )
            } else {
                Spacer(
                    Modifier
                        .windowInsetsBottomHeight(WindowInsets.navigationBars)
                        .fillMaxWidth(),
                )
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.statusBars), // Is hanled by content
        content = { innerPadding ->
            val topPadding: Dp = if (currentDestination.isRootDestination()) {
                innerPadding.calculateTopPadding()
            } else {
                0.dp
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .padding(top = topPadding)
                    .border(2.dp, Color.Green),
            ) {
                if (showNavigationRail && currentDestination.isRootDestination()) {
                    HomeNavigationRail(
                        currentDestination = currentDestination,
                        navController = navController,
                    )
                }
                NavHost(
                    navController = navController,
                    startDestination = Destination.Photos.route,
                ) {
                    composable(route = Destination.Photos.route) {
                        photos.network.ui.photos.PhotosScreen(
                            navController = navController,
                        )
                    }
                    composable(route = Destination.Albums.route) {
                        photos.network.ui.albums.AlbumsScreen(
                            navController = navController,
                        )
                    }
                    composable(route = Destination.Folders.route) {
                        photos.network.ui.folders.FoldersScreen(
                            navController = navController,
                        )
                    }
                    composable(route = Destination.Account.route) {
                        photos.network.ui.settings.SettingsScreen(
                            navController = navController,
                        )
                    }
                    composable(route = Destination.Login.route) {
                        photos.network.ui.sharing.login.LoginScreen(
                            navController = navController,
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun HomeBottomNavigation(
    currentDestination: Destination,
    navController: NavHostController,
) {
    NavigationBar {
        // Photos
        NavigationBarItem(
            icon = { Icon(Destination.Photos.icon, contentDescription = null) },
            label = { Text(stringResource(Destination.Photos.resourceId)) },
            selected = currentDestination == Destination.Photos,
            onClick = { navController.navigate(Destination.Photos.route) },
        )

        // Albums
        NavigationBarItem(
            icon = { Icon(Destination.Albums.icon, contentDescription = null) },
            label = { Text(stringResource(Destination.Albums.resourceId)) },
            selected = currentDestination == Destination.Albums,
            onClick = { navController.navigate(Destination.Albums.route) },
        )

        // Folders
        NavigationBarItem(
            icon = { Icon(Destination.Folders.icon, contentDescription = null) },
            label = { Text(stringResource(Destination.Folders.resourceId)) },
            selected = currentDestination == Destination.Folders,
            onClick = { navController.navigate(Destination.Folders.route) },
        )
    }
}

@Composable
private fun HomeNavigationRail(
    currentDestination: Destination,
    navController: NavHostController,
) {
    NavigationRail(modifier = Modifier.fillMaxHeight()) {
        // Photos
        NavigationRailItem(
            icon = { Icon(Destination.Photos.icon, contentDescription = null) },
            alwaysShowLabel = false,
            label = { Text(stringResource(Destination.Photos.resourceId)) },
            selected = currentDestination == Destination.Photos,
            onClick = { navController.navigate(Destination.Photos.route) },
        )

        // Albums
        NavigationRailItem(
            icon = { Icon(Destination.Albums.icon, contentDescription = null) },
            alwaysShowLabel = false,
            label = { Text(stringResource(Destination.Albums.resourceId)) },
            selected = currentDestination == Destination.Albums,
            onClick = { navController.navigate(Destination.Albums.route) },
        )

        // Folders
        NavigationRailItem(
            icon = { Icon(Destination.Folders.icon, contentDescription = null) },
            alwaysShowLabel = false,
            label = { Text(stringResource(Destination.Folders.resourceId)) },
            selected = currentDestination == Destination.Folders,
            onClick = { navController.navigate(Destination.Folders.route) },
        )
    }

    Divider(
        Modifier
            .fillMaxHeight()
            .width(1.dp),
    )
}

@Preview(name = "Home")
@Preview(name = "Home · DARK", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeScreen() {
    AppTheme {
        Home(
            modifier = Modifier.fillMaxSize(),
            orientation = Configuration.ORIENTATION_LANDSCAPE,
        )
    }
}
