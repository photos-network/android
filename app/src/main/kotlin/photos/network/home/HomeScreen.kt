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
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.More
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.koin.androidx.compose.viewModel
import photos.network.navigation.Destination
import photos.network.theme.AppTheme
import photos.network.ui.AlbumsScreen
import photos.network.ui.PhotosScreen
import photos.network.ui.Searchbar
import photos.network.ui.UserAvatar

/**
 * Default app screen containing a searchbar, photos grid, albums tab and more.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberAnimatedNavController(),
) {
    val viewModel: HomeViewModel by viewModel()

    val uiState by viewModel.uiState.collectAsState()

    val textState = remember { mutableStateOf(TextFieldValue()) }
    Searchbar(
        value = textState.value,
        onValueChange = { newTextFieldValue -> textState.value = newTextFieldValue },
    )

    HomeScreen(
        modifier = modifier,
        navController = navController,
        onLogout = {
            viewModel.logout()
        }
    )
}

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberAnimatedNavController(),
    onLogout: () -> Unit = {},
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scrollState: LazyListState = rememberLazyListState()

    var currentTab by rememberSaveable(saver = screenSaver()) { mutableStateOf(Destination.Photos) }
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("HomeScreenTag"),
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.systemBarsPadding()
            )
        },
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = 48.dp),
                elevation = if (scrollState.firstVisibleItemIndex < 0 || scrollState.firstVisibleItemScrollOffset < 0) 0.dp else 4.dp,
                title = {
                    UserAvatar(
                        modifier = Modifier
                            .clickable {
                                // TODO: open account sheet
                            }
                            .size(32.dp),
                        user = null
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            // TODO: show notifications
                        }
                    ) {
                        Icon(Icons.Outlined.Search, null)
                    }
                    IconButton(
                        onClick = {
                            // TODO: enable/disable Privacy filter
                        }
                    ) {
                        // TODO: change icon based on filter
                        Icon(Icons.Outlined.Shield, null)
//                        Icon(Icons.Default.Shield, null)
                    }
                    IconButton(
                        onClick = {
                            // TODO: show SettingsScreen
                        }
                    ) {
                        Icon(Icons.Outlined.MoreVert, null)
                    }
                },
                backgroundColor = MaterialTheme.colors.surface,
            )
        },
        bottomBar = {
            BottomNavigation {
                // Photos
                BottomNavigationItem(
                    icon = { Icon(Destination.Photos.icon, contentDescription = null) },
                    label = { Text(stringResource(Destination.Photos.resourceId)) },
                    selected = currentTab == Destination.Photos,
                    onClick = {
                        currentTab = Destination.Photos
                    }
                )

                // Albums
                BottomNavigationItem(
                    icon = { Icon(Destination.Albums.icon, contentDescription = null) },
                    label = { Text(stringResource(Destination.Albums.resourceId)) },
                    selected = currentTab == Destination.Albums,
                    onClick = {
                        currentTab = Destination.Albums
                    }
                )
            }
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                if (currentTab == Destination.Photos) {
                    PhotosScreen(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                    )
                } else {
                    AlbumsScreen(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                    )
                }
            }
        }
    )
}

/**
 * Saver to save and restore the current tab across config change and process death.
 */
private fun screenSaver(): Saver<MutableState<Destination>, *> = Saver(
    save = { it.value.saveState() },
    restore = { mutableStateOf(Destination.restoreState(it)) }
)

@Preview(name = "Home")
@Preview(name = "Home · DARK", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeScreen() {
    AppTheme {
        HomeScreen(
            onLogout = {}
        )
    }
}
