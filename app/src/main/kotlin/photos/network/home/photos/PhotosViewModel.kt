package photos.network.home.photos

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import photos.network.domain.photos.usecase.GetPhotosUseCase
import photos.network.domain.photos.usecase.StartPhotosSyncUseCase

class PhotosViewModel(
    application: Application,
    getPhotosUseCase: GetPhotosUseCase,
    startPhotosSyncUseCase: StartPhotosSyncUseCase,
) : AndroidViewModel(application) {
    val uiState = mutableStateOf(PhotosUiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            startPhotosSyncUseCase()

            getPhotosUseCase().collect { photos ->

                uiState.value = uiState.value.copy(
                    photos = photos,
                    isLoading = false
                )
            }
        }
    }

    fun handleEvent(event: PhotosEvent) {
        when (event) {
            PhotosEvent.DeletePhotoEvent -> TODO()
        }
    }
}
