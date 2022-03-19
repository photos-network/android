package photos.network.domain.photos.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
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

class GetPhotoUseCaseTests {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val photoRepository = mockk<PhotoRepository>()

    private val getPhotoUseCase by lazy {
        GetPhotoUseCase(
            photoRepository = photoRepository,
        )
    }

    @Test
    fun `should return available requested photo`(): Unit = runBlocking {
        // given
        val photo = createTestdata()
        every { photoRepository.getPhoto("foo.raw") } answers {
            flowOf(photo)
        }

        // when
        val result = getPhotoUseCase("foo.raw")

        // then
        Truth.assertThat(result.first()).isNotNull()
        Truth.assertThat(result.first()).isEqualTo(photo)
    }

    @Test
    fun `should return null if requested photo is not available`(): Unit = runBlocking {
        // given
        every { photoRepository.getPhoto("bar.raw") } answers {
            flowOf(null)
        }

        // when
        val result = getPhotoUseCase("bar.raw")

        // then
        Truth.assertThat(result.first()).isNull()
    }

    private fun createTestdata(): Photo {
        return Photo(
            filename = "foo.raw",
            imageUrl = "http://localhost/foo/raw",
            dateTaken = Instant.parse("2020-02-02T20:20:20Z"),
            dateAdded = Instant.parse("2020-02-02T20:20:20Z"),
            uri = null,
        )
    }
}
