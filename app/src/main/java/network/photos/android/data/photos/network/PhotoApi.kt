package network.photos.android.data.photos.network

import network.photos.android.data.photos.domain.PhotoElement
import network.photos.android.data.photos.network.model.PhotosResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface PhotoApi {

    /**
     * List all photos for current User has access to
     */
    @Headers("Accept: application/json")
    @GET("v1/photos")
    suspend fun getPhotos(
        @Header("Authorization") authToken: String?,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 50
    ): PhotosResponse

    /**
     * Load detailed information for a single photo
     *
     * @param photoId Identifier of the photo details to return
     */
    @Headers("Accept: application/json")
    @GET("v1/photo/{photoId}")
    suspend fun getPhoto(photoId: String): PhotoElement
}
