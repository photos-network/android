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
package photos.network.home.photos

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.getViewModel
import photos.network.data.photos.repository.Photo
import photos.network.theme.AppTheme
import photos.network.ui.PhotoGrid
import java.time.Instant

@Composable
fun PhotosScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val viewmodel: PhotosViewModel = getViewModel()
    val context = LocalContext.current
    val permissionState = rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    // Track if the user doesn't want to see the rationale any more.
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }

    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "To show images stored on this device, the permission to read external storage is mandatory. Please grant the permission."
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { permissionState.launchPermissionRequest() }) {
                    Text("Grant access")
                }
            }
        },
        permissionNotAvailableContent = {
            // permission denied. Please, grant access on the Settings screen.
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Read external storage is important to show images on this device. Please grant the permission."
                )
                Button(onClick = { navigateToPermissionSettings(context) }) {
                    Text("Open system settings")
                }
            }
        }
    ) {
        PhotosContent(
            modifier = modifier,
            navController = navController,
            uiState = viewmodel.uiState.collectAsState().value,
            handleEvent = viewmodel::handleEvent,
        )
    }
}

/**
 * Open app settings screen to adjust permissions
 */
private fun navigateToPermissionSettings(context: Context) {
    val intent = Intent(
        ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:${context.packageName}")
    ).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(context, intent, null)
}

@Composable
fun PhotosContent(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    uiState: PhotosUiState,
    handleEvent: (event: PhotosEvent) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                // onPause
                // TODO: stop observing media store
            } else if (event == Lifecycle.Event.ON_RESUME) {
                // onResume
                handleEvent(PhotosEvent.StartLocalPhotoSyncEvent)
                // TODO: start observing media store
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BackHandler(enabled = true){
        handleEvent(PhotosEvent.SelectIndex(null))
    }

    if (uiState.isLoading) {
        Text(
            modifier = Modifier.testTag("LOADING_SPINNER"),
            text = "Loading"
        )
    }

    PhotoGrid(
        modifier = modifier,
        photos = uiState.photos,
        selectedPhoto = uiState.selectedPhoto,
        selectedIndex = uiState.selectedIndex,
        onSelectItem = {
            handleEvent(PhotosEvent.SelectIndex(it))
        },
        selectNextPhoto = {
            handleEvent(PhotosEvent.SelectNextPhoto)
        },
        selectPreviousPhoto = {
            handleEvent(PhotosEvent.SelectPreviousPhoto)
        }
    )
    // TODO: add fast-scroll
}

@Preview(
    "Photos",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    "Photos • Dark",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun PreviewDashboard(
    @PreviewParameter(PreviewPhotosProvider::class) uiState: PhotosUiState,
) {
    AppTheme {
        PhotosContent(
            uiState = uiState,
            handleEvent = {}
        )
    }
}

internal class PreviewPhotosProvider : PreviewParameterProvider<PhotosUiState> {
    override val values = sequenceOf(
        PhotosUiState(photos = emptyList(), isLoading = true, hasError = false),
        PhotosUiState(photos = emptyList(), isLoading = false, hasError = true),
        PhotosUiState(
            photos = listOf(
                Photo(
                    filename = "0L",
                    imageUrl = "",
                    dateAdded = Instant.parse("2022-01-01T13:37:00.123Z"),
                    dateTaken = Instant.parse("2022-01-01T13:37:00.123Z")
                )
            ),
            isLoading = false,
            hasError = false
        ),
    )
    override val count: Int = values.count()
}
