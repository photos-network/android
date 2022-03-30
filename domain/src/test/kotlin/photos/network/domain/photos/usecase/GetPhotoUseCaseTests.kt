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
package photos.network.domain.photos.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import photos.network.data.photos.repository.Photo
import photos.network.data.photos.repository.PhotoRepository
import java.time.Instant

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
