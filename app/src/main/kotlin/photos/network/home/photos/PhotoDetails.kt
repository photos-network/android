package photos.network.home.photos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil.compose.rememberImagePainter
import photos.network.R
import photos.network.data.photos.repository.Photo
import photos.network.ui.PhotoBottomIcons
import photos.network.ui.PhotoTopIcons

@Composable
fun PhotoDetails(
    modifier: Modifier = Modifier,
    selectedIndex: Int?,
    selectedPhoto: Photo,
    selectNextPhoto: () -> Unit,
    selectPreviousPhoto: () -> Unit,
    onSelectItem: (index: Int?) -> Unit,
) {
    val data = if (selectedPhoto.uri != null) {
        selectedPhoto.uri
    } else {
        selectedPhoto.imageUrl
    }

    val swipeableState = rememberSwipeableState(
        initialValue = 0,
        confirmStateChange = { newValue: Int ->
            selectedIndex.let {
                if (newValue == 0) {
                    selectNextPhoto()
                } else {
                    selectPreviousPhoto()
                }
                true
            }
            false
        }
    )

    Box(modifier = modifier) {
        Image(
            modifier = Modifier
                .swipeable(
                    state = swipeableState,
                    anchors = mapOf(0f to 0, 1f to 1),
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )
                .fillMaxSize(),
            painter = rememberImagePainter(
                data = data,
                builder = {
                    crossfade(true)
                    placeholder(R.drawable.image_placeholder)
                }
            ),
            contentDescription = null,
        )

        PhotoTopIcons()

        PhotoBottomIcons()
    }
}