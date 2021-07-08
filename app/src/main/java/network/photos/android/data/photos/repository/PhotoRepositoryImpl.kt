package network.photos.android.data.photos.repository

import network.photos.android.data.photos.domain.PhotoElement
import network.photos.android.data.photos.network.PhotoApi

class PhotoRepositoryImpl(
    private val photoApi: PhotoApi
): PhotoRepository {
    override suspend fun getPhotos(page: Int): List<PhotoElement> {
        return photoApi.getPhotos().results.map {
            PhotoElement(
                id = it.id,
                name = it.name,
                owner = null,
                created_at = null,
                modified_at = null,
                details = null,
                tags = null,
                location = null,
                image_url = it.imageUrl,
            )
        }
    }
}
