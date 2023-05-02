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
import photos.network.data.settings.repository.PrivacyState
import photos.network.data.settings.repository.Settings
import photos.network.data.settings.repository.SettingsRepository
import java.time.Instant

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
        val settings = createTestSettings(privacyState = PrivacyState.NONE)
        every { settingsRepository.settings } answers {
            flowOf(settings)
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
        val settings = createTestSettings(privacyState = PrivacyState.ACTIVE)
        every { settingsRepository.settings } answers {
            flowOf(settings)
        }
        every { photoRepository.getPhotos() } answers {
            flowOf(photos)
        }

        // when
        val result = getPhotosUseCase()

        // then
        Truth.assertThat(result.first().size).isEqualTo(1)
        Truth.assertThat(result.first()).isEqualTo(photos.filterNot { it.isPrivate })
    }

    private fun createTestSettings(
        host: String = "http://localhost",
        port: Int = 443,
        clientId: String = "",
        privacyState: PrivacyState = PrivacyState.NONE,
    ): Settings {
        return Settings(
            host = host,
            port = port,
            clientId = clientId,
            privacyState = privacyState,
        )
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
                filename = "bar.raw",
                imageUrl = "http://localhost/bar/raw",
                dateTaken = Instant.parse("2020-02-02T20:20:20Z"),
                dateAdded = Instant.parse("2020-02-02T20:20:20Z"),
                isPrivate = true,
                uri = null,
            ),
        )
    }
}
