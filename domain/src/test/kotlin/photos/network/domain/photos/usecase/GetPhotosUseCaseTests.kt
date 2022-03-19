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
import photos.network.data.settings.repository.PrivacyState
import photos.network.data.settings.repository.SettingsRepository

class GetPhotosUseCaseTests {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val photoRepository = mockk<PhotoRepository>()
    private val settingsRepository = mockk<SettingsRepository>()

    private val getPhotosUseCase by lazy {
        GetPhotosUseCase(
            photoRepository = photoRepository,
            settingsRepository = settingsRepository,
        )
    }

    @Test
    fun `should return an unfiltered list of photos by default`(): Unit = runBlocking {
        // given
        val photos = createTestdata()
        every { settingsRepository.privacyState } answers {
            flowOf(PrivacyState.NONE)
        }
        every { photoRepository.getPhotos() } answers {
            flowOf(photos)
        }


        // when
        val result = getPhotosUseCase()

        // then
        Truth.assertThat(result.first().size).isEqualTo(2)
        Truth.assertThat(result.first()).isEqualTo(photos)
    }

    @Test
    fun `should return non-privacy marked photos only`(): Unit = runBlocking {
        // given
        val photos = createTestdata()
        every { settingsRepository.privacyState } answers {
            flowOf(PrivacyState.ACTIVE)
        }
        every { photoRepository.getPhotos() } answers {
            flowOf(photos)
        }

        // when
        val result = getPhotosUseCase()

        // then
        Truth.assertThat(result.first().size).isEqualTo(1)
        Truth.assertThat(result.first()).isEqualTo(photos.filter { it.isPrivate })
    }

    private fun createTestdata(): List<Photo> {
        return listOf(
            Photo(
                filename = "foo.raw",
                imageUrl = "http://localhost/foo/raw",
                dateTaken = Instant.parse("2020-02-02T20:20:20Z"),
                dateAdded = Instant.parse("2020-02-02T20:20:20Z"),
                uri = null,
            ),
            Photo(
                filename = "foo.raw",
                imageUrl = "http://localhost/foo/raw",
                dateTaken = Instant.parse("2020-02-02T20:20:20Z"),
                dateAdded = Instant.parse("2020-02-02T20:20:20Z"),
                isPrivate = true,
                uri = null,
            )
        )
    }
}
