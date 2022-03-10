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
package photos.network.details

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Divider
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel
import photos.network.R
import photos.network.theme.AppTheme

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    photoIdentifier: String
) {
    val viewmodel: DetailViewModel by viewModel()

    viewmodel.handleEvent(DetailEvent.SetIdentifier(photoIdentifier))

    DetailContent(
        modifier = modifier,
        navController = navController,
        uiState = viewmodel.uiState.value,
        handleEvent = viewmodel::handleEvent,
    )
}

@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    uiState: DetailUiState,
    handleEvent: (event: DetailEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        modifier = Modifier.testTag("DETAIL_BOTTOM_SHEET"),
        sheetBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
        scaffoldState = bottomSheetScaffoldState,
        sheetElevation = 16.dp,
        sheetShape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        sheetGesturesEnabled = true,
        sheetContent = {
            Column(modifier = Modifier.padding(16.dp)) {
                Divider(
                    modifier = Modifier.padding(horizontal = 50.dp),
                    color = MaterialTheme.colorScheme.outline,
                    thickness = 4.dp
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Identifier",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = uiState.photoIdentifier,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.heightIn(240.dp))
            }
        },
        sheetPeekHeight = 125.dp
    ) {
        val data = uiState.uri ?: uiState.imageUrl

        Column(
            modifier = modifier
                .clickable {
                    scope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        } else {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
                }
                .background(MaterialTheme.colorScheme.outline)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.isLoading) {
                Text(
                    modifier = Modifier.testTag("LOADING_SPINNER"),
                    text = "Loading"
                )
            } else {
                Image(
                    painter = rememberImagePainter(
                        data = data,
                        builder = {
                            crossfade(true)
                            placeholder(R.drawable.image_placeholder)
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(1.dp)
                )
            }
        }
    }
}

@Preview(
    "Details",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    "Details • Dark",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun PreviewDetailScreen(
    @PreviewParameter(PreviewDetailsProvider::class) uiState: DetailUiState,
) {
    AppTheme {
        DetailContent(uiState = uiState, handleEvent = {})
    }
}

internal class PreviewDetailsProvider : PreviewParameterProvider<DetailUiState> {
    override val values = sequenceOf(
        DetailUiState(isLoading = true),
        DetailUiState(isLoading = false),
    )
    override val count: Int = values.count()
}
