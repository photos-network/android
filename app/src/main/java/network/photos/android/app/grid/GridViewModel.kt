package network.photos.android.app.grid

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import network.photos.android.remote.PhotoService
import network.photos.android.remote.RetrofitService

class GridViewModel : ViewModel() {

    private val photoService = RetrofitService().createService(PhotoService::class.java)

    private val _photos = mutableStateListOf(
        "https://test.photos.network/v1/file/00a962e7-5b0e-46da-87a5-225f771b111e",
        "https://test.photos.network/v1/file/3e807f4e-5109-48a1-806d-b341663dc7c7",
        "https://test.photos.network/v1/file/cbdbd714-62e3-48f0-8c60-d1d67a3901e2",
        "https://test.photos.network/v1/file/63cdabfa-508c-4641-82ee-2b689b3345b6",
        "https://test.photos.network/v1/file/2573505b-f93c-4a07-ad31-c8715705d921",
    )
    val photos: SnapshotStateList<String>
        get() = _photos

    fun getPhotos() {
        CoroutineScope(Dispatchers.IO).launch {
            val newPhotos = photoService.getPhotos()

            CoroutineScope(Dispatchers.Main).launch {
                _photos.addAll(newPhotos.results.mapNotNull { it.image_url })
            }
        }
    }
}
