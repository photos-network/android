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
package photos.network.ui.common.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import photos.network.network.ServerStatus
import photos.network.ui.common.R
import photos.network.ui.common.theme.AppTheme

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    statusSize: Dp = size / 5,
    serverStatus: ServerStatus = ServerStatus.UNAVAILABLE,
) {
    ConstraintLayout(modifier = modifier) {
        val (logoRef, cloudRef) = createRefs()

        // Logo
        val image: Painter = painterResource(id = R.drawable.logo_inverted)
        val stateDescription = when (serverStatus) {
            ServerStatus.AVAILABLE -> stringResource(id = R.string.server_available_description)
            ServerStatus.UNAVAILABLE -> stringResource(id = R.string.server_unavailable_description)
            ServerStatus.PROGRESS -> stringResource(id = R.string.server_inprogress_description)
            ServerStatus.UNAUTHORIZED -> stringResource(id = R.string.server_unauthorized_description)
        }

        Image(
            modifier = Modifier
                .constrainAs(logoRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(size)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                .border(4.dp, Color(0xFF5DA6E3), CircleShape)
                .border(6.dp, MaterialTheme.colorScheme.background, CircleShape)
                .background(Color(0xFF5DA6E3), CircleShape)
                .padding(10.dp),
            painter = image,
            contentDescription = stateDescription,
        )

        val imageVector: Painter? = when (serverStatus) {
            ServerStatus.AVAILABLE -> null
            ServerStatus.UNAVAILABLE -> painterResource(id = R.drawable.cloud_off)
            ServerStatus.PROGRESS -> painterResource(id = R.drawable.cloud_sync)
            ServerStatus.UNAUTHORIZED -> painterResource(id = R.drawable.cloud_lock)
        }

        imageVector?.let {
            Icon(
                painter = it,
                modifier = Modifier
                    .padding(end = Dp(size / statusSize * 3))
                    .size(statusSize)
                    .constrainAs(cloudRef) {
                        bottom.linkTo(logoRef.bottom)
                        end.linkTo(logoRef.end)
                    }
                    .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
                    .padding(4.dp),

                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, name = "AppLogo")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "AppLogo • Dark")
@Composable
internal fun PreviewAppStatus(
    @PreviewParameter(PreviewServerStatusProvider::class) status: ServerStatus,
) {
    AppTheme {
        AppLogo(serverStatus = status)
    }
}

internal class PreviewServerStatusProvider : PreviewParameterProvider<ServerStatus> {
    override val values = sequenceOf(
        ServerStatus.AVAILABLE,
        ServerStatus.PROGRESS,
        ServerStatus.UNAVAILABLE,
        ServerStatus.UNAUTHORIZED,
    )
    override val count: Int = values.count()
}
