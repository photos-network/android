package photos.network.repository.photos.model

import android.graphics.PointF


data class Landmark(
    val eyebrows: PointF,
    val eyes: PointF,
    val nose: PointF,
    val mouth: PointF,
    val jawline: PointF
)
