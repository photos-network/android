package network.photos.android.app.home.photos.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.accompanist.glide.GlideImage
import network.photos.android.app.composables.AppTheme

/**
 * Photos grid
 */

@ExperimentalFoundationApi
class GridFragment : Fragment() {

    private val viewModel: GridViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        setContent {
            AppTheme {
                Column(
                    Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextButton(
                        onClick = {
                            viewModel.getPhotos()
                        },
                        Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(text = "Get Photos")
                    }
                    ImageGrid(
                        columnCount = 3
                    ) {
                        viewModel.photos.forEach {
                            GlideImage(
                                data = it,
                                contentDescription = null,
                                loading = {
                                    Box(Modifier.matchParentSize()) {
                                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                                    }
                                },
                                contentScale = ContentScale.FillWidth,
                                fadeIn = true,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(2.dp))
                            )
                        }
                    }
                }
            }
        }
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
}
