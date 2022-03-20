package photos.network.home.photos

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import photos.network.domain.photos.usecase.GetPhotosUseCase
import photos.network.domain.photos.usecase.StartPhotosSyncUseCase

class PhotosViewModel(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val startPhotosSyncUseCase: StartPhotosSyncUseCase,
) : ViewModel() {
    val uiState = mutableStateOf(PhotosUiState())

    init {
        loadPhotos()
    }

    internal fun loadPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
            getPhotosUseCase().collect { photos ->
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(
                        photos = photos,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun handleEvent(event: PhotosEvent) {
        when (event) {
            PhotosEvent.StartLocalPhotoSyncEvent -> startLocalPhotoSync()
        }
    }

    private fun startLocalPhotoSync() {
            startPhotosSyncUseCase()
        }
    }
