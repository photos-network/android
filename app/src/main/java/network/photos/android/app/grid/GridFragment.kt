package network.photos.android.app.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
                        imageURLs = viewModel.photos
                    )
                }
            }
        }
    }

    @Composable
    fun ImageGrid(
        imageURLs: List<String>
    ) {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = 90.dp),
        ) {
            items(imageURLs) { url ->
                GlideImage(
                    data = url,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(2.dp)
                        .border(2.dp, Color.Magenta, RoundedCornerShape(4.dp))
                        .padding(2.dp)
                        .clip(RoundedCornerShape(2.dp))
                )
            }
        }
    }
}
