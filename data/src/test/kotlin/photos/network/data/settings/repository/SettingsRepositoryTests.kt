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
package photos.network.data.settings.repository

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import photos.network.data.TestCoroutineDispatcherRule
import photos.network.data.settings.persistence.SettingsStorage
import photos.network.data.settings.persistence.Settings as PersistenceSettings

class SettingsRepositoryTests {
    @get:Rule
    val coroutineRule = TestCoroutineDispatcherRule()

    private val settingsStorage = mockk<SettingsStorage>()

    private val settingRepository by lazy {
        SettingsRepositoryImpl(
            settingsStore = settingsStorage
        )
    }

    @Test
    fun `should reflect settings from persistence`() = runBlocking {
        // given
        every { settingsStorage.read() } answers { fakeSettingsDto() }

        // when
        val settings = settingRepository.settings.first()

        // then
        Truth.assertThat(settings).isNotNull()
        Truth.assertThat(settings.host).isEqualTo("http://127.0.0.1")
        Truth.assertThat(settings.clientId).isEqualTo("1234567a-b89c-0d12-ef34-g5h67ij8k90l")
        Truth.assertThat(settings.privacyState).isEqualTo(PrivacyState.NONE)
    }

    @Test
    fun `recently persisted data should be kept inside cache`() = runBlocking {
        // given
        every { settingsStorage.read() } answers { fakeSettingsDto() }
        every { settingsStorage.save(any()) } returns Unit

        // when
        settingRepository.saveSettings()
        settingRepository.loadSettings()

        // then
        verify(exactly = 1) { settingsStorage.read() }
        verify(exactly = 1) { settingsStorage.save(any()) }
    }

    @Test
    fun `should create new instance if nothing is stored yet`() = runBlocking {
        // given
        every { settingsStorage.read() } answers { null }

        // when
        settingRepository.loadSettings()

        // then
        verify(exactly = 1) { settingsStorage.read() }
    }

    @Test
    fun `update settings should be persisted immediately`() = runBlocking {
        // given
        val clientId = "7654321b-c98b-0d12-ef34-g5h67ij8k90l"
        every { settingsStorage.read() } answers { fakeSettingsDto() }
        every { settingsStorage.save(any()) } returns Unit
        val settings = createTestdata(clientId = clientId)

        // when
        settingRepository.updateSettings(settings)
        val updatedSettings = settingRepository.settings.first()

        // then
        verify(exactly = 1) { settingsStorage.save(any()) }
        Truth.assertThat(updatedSettings.clientId).isEqualTo(clientId)
    }

    @Test
    fun `toggle privacy setting should change settings`() = runBlocking {
        // given
        every { settingsStorage.read() } answers { fakeSettingsDto() }
        every { settingsStorage.save(any()) } returns Unit

        // when
        val oldSettings = settingRepository.settings.first()
        settingRepository.togglePrivacy()
        val updatedSettings = settingRepository.settings.first()

        // then
        Truth.assertThat(oldSettings.privacyState).isEqualTo(PrivacyState.NONE)
        Truth.assertThat(updatedSettings.privacyState).isEqualTo(PrivacyState.ACTIVE)
    }

    @Test
    fun `update host should change settings`() = runBlocking {
        // given
        every { settingsStorage.read() } answers { fakeSettingsDto() }
        every { settingsStorage.save(any()) } returns Unit

        // when
        val oldSettings = settingRepository.settings.first()
        settingRepository.updateHost("http://10.10.10.10")
        val updatedSettings = settingRepository.settings.first()

        // then
        Truth.assertThat(oldSettings.host).isEqualTo("http://127.0.0.1")
        Truth.assertThat(updatedSettings.host).isEqualTo("http://10.10.10.10")
    }

    @Test
    fun `update clientId should change settings`() = runBlocking {
        // given
        val clientId = "7654321b-c98b-0d12-ef34-g5h67ij8k90l"
        every { settingsStorage.read() } answers { fakeSettingsDto() }
        every { settingsStorage.save(any()) } returns Unit

        // when
        val oldSettings = settingRepository.settings.first()
        settingRepository.updateClientId(clientId)
        val updatedSettings = settingRepository.settings.first()

        // then
        Truth.assertThat(oldSettings.clientId).isEqualTo("1234567a-b89c-0d12-ef34-g5h67ij8k90l")
        Truth.assertThat(updatedSettings.clientId).isEqualTo(clientId)
    }

    @Test
    fun `settings flow reflect current settings`() = runBlocking {
        // given
        every { settingsStorage.read() } answers { fakeSettingsDto() }
        every { settingsStorage.save(any()) } returns Unit

        // when
        settingRepository.togglePrivacy()
        val result1 = settingRepository.settings.first()
        settingRepository.togglePrivacy()
        val result2 = settingRepository.settings.first()

        // then
        Truth.assertThat(result1.privacyState).isEqualTo(PrivacyState.ACTIVE)
        Truth.assertThat(result2.privacyState).isEqualTo(PrivacyState.NONE)
    }

    @Test
    fun `delete should call storage`() = runBlocking {
        // given
        every { settingsStorage.read() } answers { fakeSettingsDto() }
        every { settingsStorage.save(any()) } returns Unit
        every { settingsStorage.delete() } returns Unit

        // when
        settingRepository.deleteSettings()

        // then
        verify(exactly = 1) { settingsStorage.delete() }
    }

    private fun createTestdata(
        host: String = "http://127.0.0.1",
        clientId: String = "1234567a-b89c-0d12-ef34-g5h67ij8k90l",
        privacyState: String = "NONE"
    ): Settings {
        return Settings(
            host = host,
            clientId = clientId,
            privacyState = PrivacyState.valueOf(privacyState)
        )
    }

    private fun fakeSettingsDto(
        host: String = "http://127.0.0.1",
        clientId: String = "1234567a-b89c-0d12-ef34-g5h67ij8k90l",
        privacyState: String = "NONE"
    ): PersistenceSettings = PersistenceSettings(
        host = host,
        clientId = clientId,
        privacyState = privacyState
    )
}
