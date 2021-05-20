package network.photos.android.data.photos.repository

import network.photos.android.data.photos.domain.PhotoElement
import network.photos.android.data.photos.network.PhotoService
import network.photos.android.data.photos.network.RetrofitService

class PhotoRepository {
    private val photoService = RetrofitService().createService(PhotoService::class.java)

    suspend fun getPhotos(page: Int): List<PhotoElement> {
        return photoService.getPhotos().results.map { it }
    }
}
