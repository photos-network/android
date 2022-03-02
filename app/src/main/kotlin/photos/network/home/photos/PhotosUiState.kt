package photos.network.home.photos

import photos.network.data.photos.repository.Photo

data class PhotosUiState(
    val photos: List<Photo> = emptyList(),
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
)
