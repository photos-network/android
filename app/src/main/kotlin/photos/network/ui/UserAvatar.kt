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
package photos.network.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import photos.network.R
import photos.network.repository.sharing.User
import photos.network.ui.common.theme.AppTheme

/**
 * Rounded user avatar with initials if no profile image url is available or set
 */
@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    user: User?,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .wrapContentSize(Alignment.Center)
            .clip(CircleShape)
            .background(color),
    ) {
        if (user != null) {
            if (user.profileImageUrl.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = user.profileImageUrl)
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.bob_ross_head_200x200)
                            }).build(),
                        imageLoader = LocalContext.current.imageLoader,
                    ),
                    contentDescription = stringResource(id = R.string.icon_user_profile),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds,
                )
            } else {
                val textContent = if (user.firstname.isNotBlank() && user.lastname.isNotBlank()) {
                    "${user.firstname[0]}${user.lastname[0]}"
                } else {
                    "AA"
                }

                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .padding(6.dp),
                    textAlign = TextAlign.Center,
                    text = textContent,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
        } else {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .padding(6.dp),
                textAlign = TextAlign.Center,
                text = "--",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}

@Preview(name = "UserAvatar")
@Composable
fun UserProfileImagePreview() {
    AppTheme {
        UserAvatar(
            user = User(
                firstname = "Bob",
                lastname = "Ross",
                profileImageUrl = "https://boardgaming.com/wp-content/uploads/2017/12/bob-ross-head-200x200.jpg",
            ),
        )
    }
}

@Preview(name = "UserAvatar » No Image")
@Preview(name = "UserAvatar » No Image · DARK", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UserProfileImagePreviewNoImage() {
    AppTheme {
        UserAvatar(
            user = User(firstname = "Bob", lastname = "Ross", profileImageUrl = ""),
        )
    }
}

@Preview(name = "UserAvatar » Empty")
@Composable
fun UserProfileImagePreviewNoName() {
    AppTheme {
        UserAvatar(
            user = User(firstname = "", lastname = "", profileImageUrl = ""),
        )
    }
}
