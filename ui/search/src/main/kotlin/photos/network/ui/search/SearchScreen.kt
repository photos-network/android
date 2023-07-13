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
package photos.network.ui.search

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.getViewModel
import photos.network.ui.common.navigation.Destination
import photos.network.ui.common.theme.AppTheme

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val viewmodel: SearchViewModel = getViewModel()

    SearchScreen(
        modifier = modifier,
        uiState = viewmodel.uiState.collectAsState().value,
        handleEvent = viewmodel::handleEvent,
        navigateToLogin = { navController.navigate(Destination.Login.route) },
    )
}

/**
 * stateless
 */
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    handleEvent: (event: SearchEvent) -> Unit,
    navigateToLogin: () -> Unit = {},
) {
    val verticalScrollState = rememberScrollState(0)

    Column(
        modifier = modifier
            .verticalScroll(verticalScrollState)
            .fillMaxSize(),
    ) {
        SearchHeader()

        Divider()
    }
}

@Suppress("MagicNumber")
@Composable
internal fun SearchHeader(
    modifier: Modifier = Modifier,
) {
    // header + icon
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
    ) {
        // header gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.primary)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x55000000),
                            Color(0x00000000),
                        ),
                    ),
                ),
        )

        // app name
        Text(
            modifier = Modifier
                .padding(top = 32.dp)
                .testTag("SEARCH_HEADER_TITLE")
                .fillMaxWidth(),
            text = stringResource(id = R.string.app_name_full),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = Color.White,
        )
    }
}

@Preview(
    "Account",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    "Account • Dark",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PreviewAccount(
    @PreviewParameter(PreviewAccountProvider::class) uiState: SearchUiState,
) {
    AppTheme {
        SearchScreen(
            uiState = uiState,
            handleEvent = {},
        )
    }
}

internal class PreviewAccountProvider : PreviewParameterProvider<SearchUiState> {
    override val values = sequenceOf(
        SearchUiState(
            query = "",
        ),
    )
    override val count: Int = values.count()
}
