package network.photos.android.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import network.photos.android.app.composables.AppTheme

@Preview(name = "Tags")
@Preview(name = "Tags Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TagLinePreview() {
    AppTheme {
        TagLines(
            tags = listOf("Landscape", "Architectur"),
            onClickTag = {}
        )
    }
}

@Composable
fun TagLines(
    tags: List<String>,
    onClickTag: (String) -> Unit = {}
) {
    LazyRow {
        items(tags) { tag ->
            Tag(tag, onClickTag)
            Spacer(androidx.compose.ui.Modifier.size(4.dp))
        }
    }
}
