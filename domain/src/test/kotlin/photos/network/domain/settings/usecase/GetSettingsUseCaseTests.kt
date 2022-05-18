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
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import photos.network.data.settings.repository.PrivacyState
import photos.network.data.settings.repository.Settings
import photos.network.data.settings.repository.SettingsRepository

class GetSettingsUseCaseTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val settingsRepository = mockk<SettingsRepository>()

    private val getSettingsUseCase by lazy {
        GetSettingsUseCase(
            settingsRepository = settingsRepository
        )
    }

    @Test
    fun `use case should return settings`(): Unit = runBlocking {
        // given
        every { settingsRepository.settings } answers { flowOf(fakeSettings()) }

        // when
        val result = getSettingsUseCase().first()

        // then
        Truth.assertThat(result).isNotNull()
    }

    private fun fakeSettings(
        host: String = "",
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
}
