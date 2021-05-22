package network.photos.android.data.photos.repository

import network.photos.android.data.photos.domain.PhotoElement

interface PhotoRepository {
    suspend fun getPhotos(page: Int): List<PhotoElement>
}
