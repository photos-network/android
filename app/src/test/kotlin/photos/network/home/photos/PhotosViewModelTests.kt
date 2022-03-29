package photos.network.home.photos

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Instant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import photos.network.data.photos.repository.Photo
import photos.network.domain.photos.usecase.GetPhotosUseCase
import photos.network.domain.photos.usecase.StartPhotosSyncUseCase

class PhotosViewModelTests {
    private val getPhotosUseCase = mockk<GetPhotosUseCase>()
    private val startPhotosSyncUseCase = mockk<StartPhotosSyncUseCase>()
    private val viewmodel by lazy {
        PhotosViewModel(
            getPhotosUseCase = getPhotosUseCase,
            startPhotosSyncUseCase = startPhotosSyncUseCase
        )
    }
    private val photo1 = Photo(
        filename = "filename1",
        imageUrl = "imageUrl1",
        dateTaken = Instant.parse("2022-02-02T20:20:20Z"),
        dateAdded = Instant.parse("2022-02-02T20:20:20Z"),
        uri = null,
        isPrivate = true
    )
    private val photo2 = Photo(
        filename = "filename2",
        imageUrl = "imageUrl2",
        dateTaken = Instant.parse("2022-02-02T21:21:20Z"),
        dateAdded = Instant.parse("2022-02-02T21:21:20Z"),
        uri = null,
        isPrivate = false
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewmodel should show loading indicator by default`() = runTest {
        // given
        every { getPhotosUseCase() } answers { flowOf(emptyList()) }

        // then
        Truth.assertThat(viewmodel.uiState.value.isLoading).isEqualTo(true)
    }

    @Test
    fun `viewmodel should hide loading indicator and show list of photos`() = runTest {
        // given
        every { getPhotosUseCase() } answers { flowOf(listOf(photo1, photo2)) }

        // when
        viewmodel.loadPhotos()

        // then
        Truth.assertThat(viewmodel.uiState.value).isEqualTo(
            PhotosUiState(
                photos = listOf(photo1, photo2),
                isLoading = false,
                hasError = false
            )
        )
    }

    @Test
    fun `viewmodel should start sync when opened`() {
        // given
        every { startPhotosSyncUseCase() } answers { Unit }
        every { getPhotosUseCase() } answers { flowOf(emptyList()) }

        // when
        viewmodel.handleEvent(PhotosEvent.StartLocalPhotoSyncEvent)

        // then
        verify(atLeast = 1) { startPhotosSyncUseCase.invoke() }
    }
}
