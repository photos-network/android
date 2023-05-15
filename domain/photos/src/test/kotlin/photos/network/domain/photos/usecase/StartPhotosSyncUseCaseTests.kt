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
package photos.network.domain.photos.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import photos.network.repository.photos.PhotoRepository
import photos.network.repository.photos.worker.SyncStatus

class StartPhotosSyncUseCaseTests {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val photoRepository = mockk<PhotoRepository>()

    private val startPhotosUseCase by lazy {
        StartPhotosSyncUseCase(
            photoRepository = photoRepository,
        )
    }

    @Test
    fun `start local photo sync use case should trigger sync on repository`(): Unit = runBlocking {
        // given
        coEvery { photoRepository.syncPhotos() } answers { SyncStatus.SyncSucceeded }

        // when
        startPhotosUseCase()

        // then
        coVerify(exactly = 1) { photoRepository.syncPhotos() }
    }
}
