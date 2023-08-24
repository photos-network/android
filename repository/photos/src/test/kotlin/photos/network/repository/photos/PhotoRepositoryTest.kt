/*
 * Copyright 2020-2023 Photos.network developers
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
package photos.network.repository.photos

import android.content.res.AssetManager
import androidx.work.WorkManager
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import photos.network.api.photo.PhotoApi
import photos.network.database.photos.PhotoDao
import photos.network.system.mediastore.MediaStore

/**
 * Test photo repository
 */
class PhotoRepositoryTest {

    private val assetManager = mockk<AssetManager>()
    private val photoApi = mockk<PhotoApi>()
    private val photoDao = mockk<PhotoDao>()
    private val mediaStore = mockk<MediaStore>()
    private val workManager = mockk<WorkManager>()

    private val repository by lazy {
        PhotoRepositoryImpl(
            photoApi = photoApi,
            photoDao = photoDao,
            workManager = workManager,
            mediaStore = mediaStore,
            assetManager = assetManager,
        )
    }

    @Test
    fun `should return all photos from persistence`() = runBlocking {
        // given
        every { photoDao.getPhotos() } answers {
            flowOf(
                listOf(
                    createFakePhoto(filename = "001", dateTaken = 1580671220),
                    createFakePhoto(filename = "002", dateTaken = 1580671221),
                ),
            )
        }

        // when
        val photos = repository.getPhotos().first()

        // then
        Truth.assertThat(photos.size).isEqualTo(2)
    }

    @Test
    fun `photos returned should be ordered by dateTaken`() = runBlocking {
        // given
        every { photoDao.getPhotos() } answers {
            flowOf(
                listOf(
                    createFakePhoto(filename = "002", dateTaken = 1580671221),
                    createFakePhoto(filename = "001", dateTaken = 1580671220),
                    createFakePhoto(filename = "003", dateTaken = 1580671223),
                ),
            )
        }

        // when
        val photos = repository.getPhotos().first()

        // then
        Truth.assertThat(photos[0].filename).isEqualTo("003")
        Truth.assertThat(photos[1].filename).isEqualTo("002")
        Truth.assertThat(photos[2].filename).isEqualTo("001")
    }

    @Test
    fun `photos returned should be ordered by dateAdded if dateTaken is not available`() =
        runBlocking {
            // given
            every { photoDao.getPhotos() } answers {
                flowOf(
                    listOf(
                        createFakePhoto(filename = "002", dateTaken = null, dateAdded = 1580671221),
                        createFakePhoto(filename = "001", dateTaken = null, dateAdded = 1580671220),
                        createFakePhoto(filename = "003", dateTaken = null, dateAdded = 1580671223),
                    ),
                )
            }

            // when
            val photos = repository.getPhotos().first()

            // then
            Truth.assertThat(photos[0].filename).isEqualTo("003")
            Truth.assertThat(photos[1].filename).isEqualTo("002")
            Truth.assertThat(photos[2].filename).isEqualTo("001")
        }

    private fun createFakePhoto(
        uuid: String = "001",
        filename: String = "IMG_20200202_202020.jpg",
        imageUrl: String = "http://127.0.0.1/image/e369d958-ad41-4391-9ccb-f89be8ca1e8b",
        dateAdded: Long = 1580671220,
        dateTaken: Long? = null,
        dateModified: Long? = null,
        thumbnailFileUri: String? = null,
        originalFileUri: String? = null,
    ): photos.network.database.photos.Photo {
        return photos.network.database.photos.Photo(
//            uuid = uuid,
            filename = filename,
            imageUrl = imageUrl,
            dateAdded = dateAdded,
            dateTaken = dateTaken,
            dateModified = dateModified,
//            thumbnailFileUri = thumbnailFileUri,
//            originalFileUri = originalFileUri,
        )
    }
}
