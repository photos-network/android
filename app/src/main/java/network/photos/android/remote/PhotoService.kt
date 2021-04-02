package network.photos.android.remote

import network.photos.android.remote.data.PhotoElement
import network.photos.android.remote.data.PhotoList
import retrofit2.http.GET

interface PhotoService {

    @GET("v1/photos")
    suspend fun getPhotos(): PhotoList

    @GET("v1/photo/{id}")
    suspend fun getPhoto(id: String): PhotoElement
}
