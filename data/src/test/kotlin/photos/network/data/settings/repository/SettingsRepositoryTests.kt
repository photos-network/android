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
                authCode = "4cea-82c5",
                scope = "openid profile email phone library:read",
                useSSL = false
            )
        }

        // when
        val settings = settingRepository.loadSettings()

        // then
        Truth.assertThat(settings).isNotNull()
        Truth.assertThat(settingRepository.privacyState.first()).isEqualTo(PrivacyState.NONE)
        Truth.assertThat(settingRepository.redirectUri).isEqualTo("photos://network")
        Truth.assertThat(settingRepository.clientId).isEqualTo("1234567a-b89c-0d12-ef34-g5h67ij8k90l")
        Truth.assertThat(settingRepository.clientSecret).isEqualTo("1AbcDEfgHi1JlmnOPqrStU")
        Truth.assertThat(settingRepository.authCode).isEqualTo("4cea-82c5")
        Truth.assertThat(settingRepository.scope).isEqualTo("openid profile email phone library:read")
    }
}
