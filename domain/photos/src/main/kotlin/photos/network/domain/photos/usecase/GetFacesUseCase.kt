package photos.network.domain.photos.usecase

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import photos.network.repository.photos.PhotoRepository
import photos.network.repository.photos.model.Box

class GetFacesUseCase(
    private val photoRepository: PhotoRepository,
) {
    operator fun invoke(photoUri: Uri): Flow<List<Box>> {
        return photoRepository.getFaces(photoUri = photoUri)
    }
}
