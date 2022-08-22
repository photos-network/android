package photos.network.data.photos.network

import com.google.common.truth.Truth
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import photos.network.data.settings.repository.PrivacyState
import photos.network.data.settings.repository.Settings
import photos.network.data.settings.repository.SettingsRepository

class PhotoApiTests {
    private val settingsRepository = mockk<SettingsRepository>()

    @Before
    fun setup() {
        coEvery { settingsRepository.settings } answers {
            flowOf(
                Settings(
                    host = "http://localhost",
                    privacyState = PrivacyState.NONE
                )
            )
        }
    }

    @Test
    fun `get photos should return photos for the current user`() = runBlocking {
        // given
        val photoApi = PhotoApiImpl(
            httpClient = HttpClient(MockEngine {
                respond(
                    content = ByteReadChannel("""
{
  "offset": 0,
  "limit": 50,
  "size": 1,
  "results": [
    {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "name": "filename.ext",
      "image_url": "string"
    }
  ]
}
                    """.trimIndent()),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        }, contentType = ContentType.Application.Json
                    )
                }
            },
            settingsRepository = settingsRepository
        )

        // when
        val result = photoApi.getPhotos()

        // then
        Truth.assertThat(result.size).isEqualTo(1)
    }
}
