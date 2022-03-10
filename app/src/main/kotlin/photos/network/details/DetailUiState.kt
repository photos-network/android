package photos.network.details

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.material.ModalBottomSheetValue
import java.time.LocalDateTime

data class DetailUiState(
    val orientation: Int = Configuration.ORIENTATION_PORTRAIT,
    val isLoading: Boolean = true,
    val uri: Uri? = null,
    val imageUrl: String? = null,
    val photoIdentifier: String = "",
    val isImmersive: Boolean = true,
    val dateTime: LocalDateTime? = null,
    val location: String? = null,
    val tags: List<String>? = emptyList(),
    val sizeInBytes: Long? = null,
    val resolution: Map<Int, Int>? = mapOf(),
)
