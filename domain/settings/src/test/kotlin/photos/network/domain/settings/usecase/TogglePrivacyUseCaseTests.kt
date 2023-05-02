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
package photos.network.domain.settings.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import photos.network.repository.settings.SettingsRepository

class TogglePrivacyUseCaseTests {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val settingsRepository = mockk<SettingsRepository>()

    private val togglePrivacyUseCase by lazy {
        TogglePrivacyUseCase(
            settingsRepository = settingsRepository,
        )
    }

    @Test
    fun `use case update privacy settings in repository`(): Unit = runBlocking {
        // given
        coEvery { settingsRepository.togglePrivacy() } answers { }

        // when
        togglePrivacyUseCase()

        // then
        coVerify(atLeast = 1, atMost = 1) { settingsRepository.togglePrivacy() }
    }
}
