package photos.network.details

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.material.ModalBottomSheetValue
import java.time.LocalDateTime

data class DetailUiState(
    // display options
    val orientation: Int = Configuration.ORIENTATION_PORTRAIT,

    // ui elements
    val isLoading: Boolean = true,
    val isImmersive: Boolean = true,

    // content to show
    val uri: Uri? = null,
    val imageUrl: String? = null,

    // privacy
    // favorite

    // details
    val dateTime: LocalDateTime? = null,

    val photoIdentifier: String = "",
    val pixelCount: Float? = null,
    val resolution: Map<Int, Int>? = mapOf(),
    val sizeInBytes: Long? = null,

    val cameraManufacturer: String? = null,     // Nikon
    val cameraModel: String? = null,            // Z7II
    val colorProfile: String? = null,           // sRGB


    val aperture: Float? = null,                // f 2.0
    val focalLength: Float? = null,             // 50mm
    val exposureTime: Float? = null,            // 10000
    val exposureIndex: Float? = null,           // ISO 800

    val locationString: String? = null,         // Reichstag Berlin
    val location: Map<Long, Long>? = null,      // 52.51859, 13.37617  =>  lat: 0 to 90, lon: -180 to +180

    // manual assigned tags by user
    val tags: List<String>? = emptyList(),

    // automatic recognitions
    val recognitions: List<Recognition>? = emptyList(),
)

data class Recognition(
    val label: String,
    val type: RecognitionType = RecognitionType.UNKNOWN,
    val topLeft: Float? = null,
    val bottomRight: Float? = null,
)

enum class RecognitionType {
    UNKNOWN,
    ANIMAL,
    HUMAN,
    TECHNICAL
}
