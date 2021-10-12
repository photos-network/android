package network.photos.android.data.photos.repository

import network.photos.android.data.Resource
import network.photos.android.data.photos.domain.PhotoElement
import network.photos.android.data.user.domain.Token

interface PhotoRepository {
    suspend fun getPhotos(
        token: Token? = null,
        page: Int
    ): Resource<List<PhotoElement>>
}
