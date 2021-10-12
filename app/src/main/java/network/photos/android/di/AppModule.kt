package network.photos.android.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import network.photos.android.data.AuthInterceptor
import network.photos.android.data.photos.network.PhotoApi
import network.photos.android.data.photos.repository.PhotoRepository
import network.photos.android.data.photos.repository.PhotoRepositoryImpl
import network.photos.android.data.settings.repository.SettingsRepository
import network.photos.android.data.settings.repository.SettingsRepositoryImpl
import network.photos.android.data.settings.storage.SettingsStorage
import network.photos.android.data.user.network.UserApi
import network.photos.android.data.user.repository.UserRepository
import network.photos.android.data.user.repository.UserRepositoryImpl
import network.photos.android.data.user.storage.UserStorage
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providesOkHttp(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.HEADERS
                it.level = HttpLoggingInterceptor.Level.BODY
            }

//        val authInterceptor = AuthInterceptor(userRepository)

        return OkHttpClient.Builder()
            .addInterceptor(interceptor = loggingInterceptor)
//            .authenticator(authenticator = authInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .cache(
                cache = Cache(
                    directory = File(context.cacheDir, "http_cache"),
                    maxSize = 20L * 1024 * 1024
                )
            )
            .build()
    }

    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        settingsRepository: SettingsRepository
    ): Retrofit {
        val settings = settingsRepository.loadSettings()
        val host = settings?.host ?: "https://photos.network"

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(host)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    fun providesUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    fun providesPhotoApi(retrofit: Retrofit): PhotoApi = retrofit.create(PhotoApi::class.java)

    @Provides
    fun provideUserRepository(
        @ApplicationContext context: Context,
        userApi: UserApi
    ): UserRepository {
        return UserRepositoryImpl(
            userApi = userApi,
            userStorage = UserStorage(context)
        )
    }

    @Provides
    fun providePhotoRepository(
        photoApi: PhotoApi
    ): PhotoRepository {
        return PhotoRepositoryImpl(
            photoApi = photoApi
        )
    }

    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepositoryImpl(
            settingsStore = SettingsStorage(context)
        )
    }
}
