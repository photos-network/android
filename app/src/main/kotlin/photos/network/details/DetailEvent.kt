package photos.network.details

import androidx.compose.material.ModalBottomSheetValue

sealed class DetailEvent {
    class SetIdentifier(val identifier: String): DetailEvent()
    object Share: DetailEvent()
    object Delete: DetailEvent()
}
