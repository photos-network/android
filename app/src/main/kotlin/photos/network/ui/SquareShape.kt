package photos.network.ui

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class SquareShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val max = maxOf(size.width, size.height)

            moveTo(0f, 0f)
            lineTo(max, 0f)
            lineTo(max, max)
            lineTo(0f, max)
            lineTo(0f, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}
