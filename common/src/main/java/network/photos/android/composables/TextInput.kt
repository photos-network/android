package network.photos.android.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    label: String?,
    name: String, /* state */
    enabled: Boolean = true,
    onNameChange: (String) -> Unit /* event */
) {
    Column {
        TextField(
            modifier = modifier.padding(vertical = 8.dp),
            enabled = enabled,
            value = name,
            onValueChange = onNameChange,
            label = { Text(label ?: "") }
        )
    }
}
