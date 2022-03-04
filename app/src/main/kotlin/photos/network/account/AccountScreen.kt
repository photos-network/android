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
package photos.network.account

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.getViewModel
import photos.network.R
import photos.network.theme.AppTheme
import photos.network.ui.components.AppLogo

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val viewmodel: AccountViewModel = getViewModel()

    AccountContent(
        modifier = modifier,
        navController = navController,
        uiState = viewmodel.uiState.value,
        handleEvent = viewmodel::handleEvent,
    )
}

@Composable
fun AccountContent(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    uiState: AccountUiState,
    handleEvent: (event: AccountEvent) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        // header + icon
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // header gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF5DA6E3))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x55000000),
                                Color(0x00000000)
                            )
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 125.dp)
            ) {
                AppLogo(
                    modifier = Modifier.align(Alignment.Center),
                    size = 150.dp,
                )
            }
        }

        // app name
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.app_name_full),
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
        )

        // description
        Text(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.app_description),
            textAlign = TextAlign.Center,
        )

        // buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        // TODO: handle click
                    }
            ) {
                IconButton(
                    modifier = Modifier
                        .background(Color(0xFFECEDF0), CircleShape)
                        .align(Alignment.CenterHorizontally),
                    onClick = {}) {
                    Icon(Icons.Filled.Phone, null)
                }
                Text("Server")
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        // TODO: handle click
                    }
            ) {
                IconButton(
                    modifier = Modifier
                        .background(Color(0xFFECEDF0), CircleShape)
                        .align(Alignment.CenterHorizontally),
                    onClick = {}) {
                    Icon(Icons.Filled.Person, null)
                }
                Text("Edit Profile")
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        // TODO: handle click
                    }
            ) {
                IconButton(
                    modifier = Modifier
                        .background(Color(0xFFECEDF0), CircleShape)
                        .align(Alignment.CenterHorizontally),
                    onClick = {}) {
                    Icon(Icons.Filled.List, null)
                }
                Text("Activity Log")
            }
        }
    }

    // TODO: bottom sheet for server connection settings
}

@Preview(
    "Account",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    "Account • Dark",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun PreviewAccount(
    @PreviewParameter(PreviewAccountProvider::class) uiState: AccountUiState,
) {
    AppTheme {
        AccountContent(
            uiState = uiState,
            handleEvent = {}
        )
    }
}

internal class PreviewAccountProvider : PreviewParameterProvider<AccountUiState> {
    override val values = sequenceOf(
        AccountUiState(serverStatus = ServerStatus.PROGRESS),
        AccountUiState(serverStatus = ServerStatus.UNAVAILABLE),
        AccountUiState(serverStatus = ServerStatus.AVAILABLE),
    )
    override val count: Int = values.count()
}
