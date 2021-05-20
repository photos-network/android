package network.photos.android.data.photos.network

import network.photos.android.data.photos.domain.PhotoElement
import network.photos.android.data.photos.domain.PhotoList
import retrofit2.http.GET

interface PhotoService {

    @GET("v1/photos")
    suspend fun getPhotos(): PhotoList

    @GET("v1/photo/{id}")
    suspend fun getPhoto(id: String): PhotoElement
}
