package network.photos.android.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import network.photos.android.app.composables.AppTheme
import network.photos.android.common.R

@Preview(name = "UserProfile")
@Composable
fun UserProfileImagePreview() {
    AppTheme {
        UserProfileImage(
            user = User(
                "Bob",
                "Foo",
                profileImageUrl = "https://boardgaming.com/wp-content/uploads/2017/12/bob-ross-head-200x200.jpg"
            )
        )
    }
}

@Preview(name = "UserProfileEmpty")
@Composable
fun UserProfileImagePreviewNoImage() {
    AppTheme {
        UserProfileImage(
            user = User("Bob", "Ross", profileImageUrl = "")
        )
    }
}

@Preview(name = "UserProfile")
@Composable
fun UserProfileImagePreviewNoName() {
    AppTheme {
        UserProfileImage(
            user = User(
                "",
                "",
                profileImageUrl = ""
            )
        )
    }
}

@Composable
fun UserProfileImage(
    user: User,
    color: Color = MaterialTheme.colors.primary
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color)
    ) {
        if (user.profileImageUrl.isNotBlank()) {
            Image(
                painter = rememberGlidePainter(
                    request = user.profileImageUrl,
                    fadeIn = true,
                ),
                contentDescription = stringResource(id = R.string.icon_user_profile),
                modifier = Modifier.clip(CircleShape),
                contentScale = ContentScale.FillBounds,
            )
        } else {
            if (user.firstName.isNotBlank() && user.lastName.isNotBlank()) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "${user.firstName[0]}${user.lastName[0]}",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h3
                )
            } else {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "AA",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h3
                )
            }
        }
    }
}

data class User(
    val firstName: String,
    val lastName: String,
    val profileImageUrl: String
)
