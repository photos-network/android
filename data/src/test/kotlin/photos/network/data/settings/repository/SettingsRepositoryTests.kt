package photos.network.data.settings.repository

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import photos.network.data.TestCoroutineDispatcherRule
import photos.network.data.settings.persistence.SettingsStorage
import photos.network.data.settings.persistence.entities.SettingsDto

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
        every { settingsStorage.read() } answers {
            SettingsDto(
                host = "http://127.0.0.1",
                redirectUri = "photos://network",
                clientId =     "1234567a-b89c-0d12-ef34-g5h67ij8k90l",
                clientSecret = "1AbcDEfgHi1JlmnOPqrStU",
                authCode = "",
                scope = "",
                useSSL = false
            )
        }

        // when
        val settings = settingRepository.loadSettings()

        // then
        Truth.assertThat(settings).isNotNull()
        Truth.assertThat(settingRepository.privacyState.first()).isEqualTo(PrivacyState.NONE)
    }
}
