package network.photos.android.app.home.photos

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.accompanist.flowlayout.FlowColumn
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import network.photos.android.app.home.HomeViewModel
import network.photos.android.data.photos.domain.PhotoElement
import network.photos.android.navigation.Destination

@Composable
fun PhotosScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberAnimatedNavController(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    PhotosScreen(
        modifier = modifier,
        navController = navController,
        isLoading = uiState.loading,
        photos = uiState.photos,
        onRefreshPhotos = {
            viewModel.refreshPhotos()
        },
    )
}

@Composable
fun PhotosScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberAnimatedNavController(),
    isLoading: Boolean = false,
    photos: List<PhotoElement> = emptyList(),
    onRefreshPhotos: () -> Unit = {},
) {
//    Column(
//        modifier.padding(4.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
    FlowRow(
        modifier = modifier,
        mainAxisSpacing = 1.dp,
//        crossAxisSpacing = 1.dp,
        content = {
            photos.forEach { data ->
                Image(
                    painter = rememberImagePainter(
                        data = data.image_url,
                        builder = {
                            crossfade(true)
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
//                            .placeholder(
//                                visible = true,
//                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.LightGray),
//                                color = Color.Gray
//                            )
                        .clickable {
                            navController.navigate("${Destination.Details.route}/${data.id}")
                        }
                        .size(128.dp)
//                        .padding(2.dp)
                        .clip(RoundedCornerShape(2.dp)),
//                    contentScale = ContentScale.FillWidth,
                )
            }
        }
    )
//            ImageGrid(
//                columnCount = 3
//            ) {

//            }
//        }
    }

@Composable
fun ImageGrid(
    modifier: Modifier = Modifier,
    columnCount: Int,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        modifier = modifier

            .verticalScroll(rememberScrollState(0)),
    ) { measurables, constraints ->
        val placeableXY: MutableMap<Placeable, Pair<Int, Int>> = mutableMapOf()

        val columnWidth = constraints.maxWidth / columnCount
        val childConstraints = constraints.copy(maxWidth = columnWidth)

        val columnHeights = IntArray(columnCount) { 0 }
        val placeables = measurables.map {
            val column = shortestColumn(columnHeights)
            val placeable = it.measure(childConstraints)

            placeableXY[placeable] = (columnWidth * column) to columnHeights[column]

            columnHeights[column] += placeable.height
            placeable
        }

        val layoutHeight = columnHeights
            .maxOrNull()
            ?.coerceIn(constraints.minHeight, constraints.maxHeight) ?: constraints.minHeight

        layout(
            width = constraints.maxWidth,
            height = layoutHeight
        ) {
            placeables.forEach {
                it.place(placeableXY.getValue(it).first, placeableXY.getValue(it).second)
            }
        }
    }
}

private fun shortestColumn(colHeights: IntArray): Int {
    var minHeight = Int.MAX_VALUE
    var column = 0
    colHeights.forEachIndexed { index, height ->
        if (height < minHeight) {
            minHeight = height
            column = index
        }
    }
    return column
}
