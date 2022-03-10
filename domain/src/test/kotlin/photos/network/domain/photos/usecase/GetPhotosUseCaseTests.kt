package photos.network.domain.photos.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import java.time.Instant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import photos.network.data.photos.repository.Photo
import photos.network.data.photos.repository.PhotoRepository
import photos.network.data.user.repository.UserRepository

class GetPhotosUseCaseTests {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val photoRepository = mockk<PhotoRepository>()
    private val userRepository = mockk<UserRepository>()

    private val getPhotosUseCase by lazy {
        GetPhotosUseCase(
            userRepository = userRepository,
            photoRepository = photoRepository
        )
    }

    @Test
    fun `use case should return list of photos as flow`(): Unit = runBlocking {
        // given
        val testPhoto = Photo(
            filename = "foo.raw",
            imageUrl = "http://localhost/foo/raw",
            dateTaken = Instant.parse("2020-02-02T20:20:20Z"),
            dateAdded = Instant.parse("2020-02-02T20:20:20Z"),
            uri = null,
        )
        every { photoRepository.getPhotos() } answers {
            flowOf(
                listOf(
                    testPhoto
                )
            )
        }

        // when
        val result = getPhotosUseCase.invoke()

        // then
        assertThat(result.first()).isEqualTo(listOf(testPhoto))
    }
}
