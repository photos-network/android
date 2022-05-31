package photos.network.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import photos.network.theme.JetbrainsMono

@Composable
fun ActivityLog(
    modifier: Modifier = Modifier,
) {
    TextField(
        modifier = modifier,
        value = TextFieldValue(
            text = "15:40:46 Lorem ipsum dolor sit amet"
        ),
        singleLine = false,
        maxLines = Int.MAX_VALUE,
        textStyle = TextStyle(
            fontFamily = JetbrainsMono,
            fontWeight = FontWeight.Thin,
            fontSize = 10.sp,
            lineHeight = 1.2.em,
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0xAED7D7D7),
            textColor = Color(0xFF000000),
        ),
        shape = RectangleShape,
        enabled = false,
        readOnly = true,
        label = null,
        placeholder = null,
        leadingIcon = null,
        trailingIcon = null,
        isError = false,
        visualTransformation = FormattedTextTransformation(),
        onValueChange = {}
    )
}

class FormattedTextTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        val formattedText = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                originalText.substring(0, 8)
            }
            append(
                originalText.substring(9)
            )
        }

        return TransformedText(
            text = formattedText,
            offsetMapping = OffsetMapping.Identity
        )
    }
}

@Preview
@Composable
internal fun ActivityLogPreview() {
    MaterialTheme {
        ActivityLog()
    }
}
