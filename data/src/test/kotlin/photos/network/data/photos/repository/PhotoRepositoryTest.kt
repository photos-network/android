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
package photos.network.data.photos.repository

import android.content.Context
import androidx.work.WorkManager
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import photos.network.data.TestCoroutineDispatcherRule
import photos.network.data.photos.network.PhotoApi
import photos.network.data.photos.persistence.Photo
import photos.network.data.photos.persistence.PhotoDao

/**
 * Test photo repository
 */
class PhotoRepositoryTest {

    @get:Rule
    val coroutineRule = TestCoroutineDispatcherRule()

    private val applicationContext = mockk<Context>()
    private val photoApi = mockk<PhotoApi>()
    private val photoDao = mockk<PhotoDao>()
    private val workManager = mockk<WorkManager>()

    @Test
    fun `should return all photos from persistence`() = runBlocking {
        // given
        every { photoDao.getPhotos() } answers {
            flowOf(
                listOf(
                    Photo(
                        uuid = null,
                        filename = "IMG_20200202_202020.jpg",
                        imageUrl = "http://127.0.0.1/image/e369d958-ad41-4391-9ccb-f89be8ca1e8b",
                        dateTaken = 1580671220,
                        dateAdded = 1580671220,
                        dateModified = 1580671220,
                        thumbnailFileUri = null,
                        originalFileUri = null
                    )
                )
            )
        }
        val repository = PhotoRepositoryImpl(
            applicationContext = applicationContext,
            photoApi = photoApi,
            photoDao = photoDao,
            workManager = workManager,
        )

        // when
        val photos = repository.getPhotos().first()

        // then
        Truth.assertThat(photos.size).isEqualTo(1)
    }
}
