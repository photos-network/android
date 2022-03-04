package photos.network.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudUpload
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
import photos.network.R
import photos.network.account.ServerStatus
import photos.network.theme.AppTheme

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    serverStatus: ServerStatus = ServerStatus.UNAVAILABLE,
) {
    ConstraintLayout {
        val (logoRef, cloudRef) = createRefs()

        // Logo
        val image: Painter = painterResource(id = R.drawable.logo_inverted)
        val stateDescription = when (serverStatus) {
            ServerStatus.AVAILABLE -> stringResource(id = R.string.server_available_description)
            ServerStatus.UNAVAILABLE -> stringResource(id = R.string.server_unavailable_description)
            ServerStatus.PROGRESS -> stringResource(id = R.string.server_inprogress_description)
        }
        Image(
            modifier = modifier
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
            contentDescription = stateDescription
        )

        val imageVector = when (serverStatus) {
            ServerStatus.AVAILABLE -> null
            ServerStatus.UNAVAILABLE -> Icons.Filled.CloudOff
            ServerStatus.PROGRESS -> Icons.Filled.CloudUpload
        }

        imageVector?.let {
            Icon(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(18.dp)
                    .constrainAs(cloudRef) {
                        bottom.linkTo(logoRef.bottom)
                        end.linkTo(logoRef.end)
                    }
                    .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
                    .padding(4.dp),
                imageVector = it,
                contentDescription = null,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "AppLogo")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "AppLogo • Dark")
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
        ServerStatus.PROGRESS,
        ServerStatus.UNAVAILABLE,
        ServerStatus.AVAILABLE,
    )
    override val count: Int = values.count()
}
