package network.photos.android.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

    private val gson = GsonBuilder().create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://test.photos.network/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun <Service : Any> createService(serviceClass: Class<Service>): Service {
        return retrofit.create(serviceClass)
    }
}
