package network.photos.android.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import network.photos.android.theme.AppTheme
import network.photos.android.R

/**
 * Tag used for categories etc.
 */
@Composable
fun Tag(
    tag: String,
    onClickTag: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.secondaryVariant,
                shape = RoundedCornerShape(topStartPercent = 50, bottomStartPercent = 1)
            )
            .clip(RoundedCornerShape(topStartPercent = 50))
            .clickable(onClick = { onClickTag(tag) })
            .padding(horizontal = 8.dp)

    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(vertical = 4.dp),
            imageVector = Icons.Default.Bookmarks,
            contentDescription = stringResource(id = R.string.icon_tags)
        )
        Text(
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
            text = tag,
            fontSize = MaterialTheme.typography.caption.fontSize,
            color = MaterialTheme.colors.onSecondary
        )
    }
}

@Preview(name = "Tag")
@Preview(name = "Tag Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TagPreview() {
    AppTheme {
        Tag(
            tag = "Landscape",
            onClickTag = {}
        )
    }
}
