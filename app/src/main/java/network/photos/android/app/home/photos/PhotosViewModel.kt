package network.photos.android.app.home.photos

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import network.photos.android.data.photos.repository.PhotoRepository
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _photos = mutableStateListOf<String>()
    val photos: SnapshotStateList<String>
        get() = _photos

    init {
        viewModelScope.launch {
            val photos = photoRepository.getPhotos(0)
            photos.forEach { photo ->
                _photos.add(photo.image_url)
            }
        }
    }

    fun getPhotos() {
        viewModelScope.launch {
            // refresh photos
        }
    }
}
