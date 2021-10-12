package network.photos.android.app.home

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import network.photos.android.app.home.photos.GridScreen
import network.photos.android.composables.UserAvatar
import network.photos.android.data.photos.domain.PhotoElement
import network.photos.android.data.user.domain.User
import network.photos.android.navigation.Destination
import network.photos.android.theme.AppTheme

/**
 * Default app screen containing a searchbar, photos grid, albums tab and more.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberAnimatedNavController(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    viewModel.loadSettings()
    if (uiState.currentSettings == null) {
        Log.i("Home", "settings not loaded yet! => Setup")
        navController.navigate(route = Destination.Setup.route) {
            launchSingleTop = true
        }
        return
    }

    viewModel.loadUser()
    if (uiState.currentUser == null) {
        Log.i("Home", "no user logged in! => Login")
        navController.navigate(route = Destination.Login.route) {
            launchSingleTop = true
        }
        return
    } else {
        HomeScreen(
            modifier = modifier,
            navController = navController,
            isLoading = uiState.loading,
            user = uiState.currentUser!!,
            photos = uiState.photos,
            onRefreshPhotos = {
                viewModel.refreshPhotos()
            },
            onLogout = {
                viewModel.logout()
            }
        )
    }
}

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberAnimatedNavController(),
    isLoading: Boolean = false,
    user: User,
    photos: List<PhotoElement> = emptyList(),
    onRefreshPhotos: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scrollState: LazyListState = rememberLazyListState()

    val items = listOf(
        Destination.Photos,
        Destination.Albums,
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("HomeScreenTag"),
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = it, modifier = Modifier.systemBarsPadding()) },
        topBar = {
            TopAppBar(
                elevation = if (scrollState.firstVisibleItemIndex < 0 || scrollState.firstVisibleItemScrollOffset < 0) 0.dp else 4.dp,
                title = {
                    UserAvatar(
                        modifier = Modifier
                            .clickable {
                                onLogout()
                            }
                            .size(32.dp),
                        user = user
                    )
                },
                actions = {
                    IconButton(onClick = {
                        // TODO: show notifications
                    }) {
                        Icon(Icons.Outlined.Notifications, null)
                    }
                    IconButton(onClick = {
                        // TODO: enable/disable NSFW
                    }) {
                        Icon(Icons.Outlined.Shield, null)
                    }
                    IconButton(onClick = {
                        // TODO: show SettingsScreen
                    }) {
                        Icon(Icons.Outlined.Settings, null)
                    }
                },
                backgroundColor = MaterialTheme.colors.surface,
            )
        },
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        content = {
            SwipeRefresh(
                modifier = Modifier.fillMaxSize(),
                state = rememberSwipeRefreshState(isLoading),
                onRefresh = { onRefreshPhotos() },
            ) {
                // TODO: add bottom navigation (photos / albums)
                GridScreen(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    photos = photos,
                )
            }
        }
    )
}

@Preview(name = "Home")
@Preview(name = "Home · DARK", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeScreen() {
    AppTheme {
        HomeScreen()
    }
}
