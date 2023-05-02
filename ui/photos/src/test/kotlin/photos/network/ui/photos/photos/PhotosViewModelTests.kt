/*
 * Copyright 2020-2022 Photos.network developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package photos.network.ui.photos.photos

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import photos.network.data.photos.repository.Photo
import photos.network.domain.photos.usecase.GetPhotosUseCase
import photos.network.domain.photos.usecase.StartPhotosSyncUseCase
import java.time.Instant

class PhotosViewModelTests {
    private val getPhotosUseCase = mockk<GetPhotosUseCase>()
    private val startPhotosSyncUseCase = mockk<StartPhotosSyncUseCase>()
    private val viewmodel by lazy {
        photos.network.ui.photos.PhotosViewModel(
            getPhotosUseCase = getPhotosUseCase,
            startPhotosSyncUseCase = startPhotosSyncUseCase,
        )
    }
    private val photo1 = Photo(
        filename = "filename1",
        imageUrl = "imageUrl1",
        dateTaken = Instant.parse("2022-02-02T20:20:20Z"),
        dateAdded = Instant.parse("2022-02-02T20:20:20Z"),
        uri = null,
        isPrivate = true,
    )
    private val photo2 = Photo(
        filename = "filename2",
        imageUrl = "imageUrl2",
        dateTaken = Instant.parse("2022-02-02T21:21:20Z"),
        dateAdded = Instant.parse("2022-02-02T21:21:20Z"),
        uri = null,
        isPrivate = false,
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
    fun `viewmodel should hide loading indicator and show list of photos`() = runTest {
        // given
        every { getPhotosUseCase() } answers { flowOf(listOf(photo1, photo2)) }

        // when
        viewmodel.loadPhotos()

        // then
        Truth.assertThat(viewmodel.uiState.value).isEqualTo(
            photos.network.ui.photos.PhotosUiState(
                photos = listOf(photo1, photo2),
                isLoading = false,
                hasError = false,
            ),
        )
    }

    @Test
    fun `viewmodel should start sync when opened`() {
        // given
        every { startPhotosSyncUseCase() } answers { Unit }
        every { getPhotosUseCase() } answers { flowOf(emptyList()) }

        // when
        viewmodel.handleEvent(photos.network.ui.photos.PhotosEvent.StartLocalPhotoSyncEvent)

        // then
        verify(atLeast = 1) { startPhotosSyncUseCase.invoke() }
    }
}
