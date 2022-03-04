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
package photos.network.home.photos

import android.content.res.Configuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import java.time.Instant
import org.koin.androidx.compose.getViewModel
import photos.network.data.photos.repository.Photo
import photos.network.theme.AppTheme
import photos.network.ui.PhotoGrid

@Composable
fun PhotosScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val viewmodel: PhotosViewModel = getViewModel()

    PhotosContent(
        modifier = modifier,
        navController = navController,
        uiState = viewmodel.uiState.value,
        handleEvent = viewmodel::handleEvent,
    )
}

@Composable
fun PhotosContent(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    uiState: PhotosUiState,
    handleEvent: (event: PhotosEvent) -> Unit,
) {
    if (uiState.isLoading) {
        Text(
            modifier = Modifier.testTag("LOADING_SPINNER"),
            text = "Loading"
        )
    }

    PhotoGrid(
        modifier = modifier,
        photos = uiState.photos,
        onSelectItem = {
            // Handle selection
            // navController.navigate("${Destination.Details.route}/${photos[index].id}")
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
            photos = listOf(Photo("0L", "", Instant.parse("2022-01-01T13:37:00.123Z"))),
            isLoading = false,
            hasError = false
        ),
    )
    override val count: Int = values.count()
}
