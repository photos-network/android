package network.photos.android

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
object DependencyInjectionModule {
    @Provides
    fun providesOkHttp(
        @ApplicationContext context: Context
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().also { it.setLevel(HttpLoggingInterceptor.Level.BODY) }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .cache(Cache(File(context.cacheDir, "http_cache"), 20 * 1024 * 1024))
            .build()
    }

    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
        settingsRepository: SettingsRepository
    ): Retrofit {
        val settings = settingsRepository.loadSettings()
        val host = settings?.host ?: "https://photos.network"

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(host)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun providersMoshi(): Moshi = Moshi.Builder().build()

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
        @ApplicationContext context: Context,
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
