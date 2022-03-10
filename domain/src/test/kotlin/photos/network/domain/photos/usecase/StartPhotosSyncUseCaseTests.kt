package photos.network.domain.photos.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import photos.network.data.photos.repository.PhotoRepository

class StartPhotosSyncUseCaseTests {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val photoRepository = mockk<PhotoRepository>()

    private val startPhotosUseCase by lazy {
        StartPhotosSyncUseCase(
            photoRepository = photoRepository
        )
    }

    @Test
    fun `start local photo sync use case should trigger sync on repository`(): Unit = runBlocking {
        // given
        every { photoRepository.syncPhotos() } answers {}

        // when
        startPhotosUseCase()

        // then
        verify(exactly = 1) { photoRepository.syncPhotos() }
    }
}
