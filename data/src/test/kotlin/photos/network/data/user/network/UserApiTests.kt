package photos.network.data.user.network

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
import photos.network.data.user.persistence.User
import photos.network.data.user.persistence.UserStorage

class UserApiTests {
    private val userStorage = mockk<UserStorage>()
    private val settingsRepository = mockk<SettingsRepository>()

    @Before
    fun setup() {
        coEvery { userStorage.read() } answers {
            User(
                id = "id123",
                lastname = "Lastname",
                firstname = "Firstname",
                profileImageUrl = ""
            )
        }
        coEvery { userStorage.save(any()) } answers {}
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
    fun `verify server host should succeed`() = runBlocking {
        // given
        val userApi = UserApiImpl(
            httpClient = HttpClient(
                MockEngine {
                    respond(
                        content = ByteReadChannel(
                            """
{
  "message": "API running"
}
                            """.trimIndent()
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            ) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                        contentType = ContentType.Application.Json
                    )
                }
            },
            userStorage = userStorage,
            settingsRepository = settingsRepository
        )

        // when
        val result = userApi.verifyServerHost("localhost")

        // then
        Truth.assertThat(result).isEqualTo(true)
    }

    @Test
    fun `verify invalid server host should fail`() = runBlocking {
        // given
        val userApi = UserApiImpl(
            httpClient = HttpClient(
                MockEngine {
                    respond(
                        content = ByteReadChannel("""{}""".trimIndent()),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            ) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                        contentType = ContentType.Application.Json
                    )
                }
            },
            userStorage = userStorage,
            settingsRepository = settingsRepository
        )

        // when
        val result = userApi.verifyServerHost("invalid")

        // then
        Truth.assertThat(result).isEqualTo(false)
    }

    @Test
    fun `verify client id should succeed`() = runBlocking {
        // given
        val userApi = UserApiImpl(
            httpClient = HttpClient(
                MockEngine {
                    respond(
                        content = ByteReadChannel("""{}""".trimIndent()),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            ) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                        contentType = ContentType.Application.Json
                    )
                }
            },
            userStorage = userStorage,
            settingsRepository = settingsRepository
        )

        // when
        val result = userApi.verifyClientId("clientID")

        // then
        Truth.assertThat(result).isEqualTo(true)
    }

    @Test
    fun `verify invalid client id should fail`() = runBlocking {
        // given
        val userApi = UserApiImpl(
            httpClient = HttpClient(
                MockEngine {
                    respond(
                        content = ByteReadChannel("""{}""".trimIndent()),
                        status = HttpStatusCode.PreconditionFailed,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            ) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                        contentType = ContentType.Application.Json
                    )
                }
            },
            userStorage = userStorage,
            settingsRepository = settingsRepository
        )

        // when
        val result = userApi.verifyClientId("clientID")

        // then
        Truth.assertThat(result).isEqualTo(false)
    }

    @Test
    fun `request access token for valid authCode should succeed`() = runBlocking {
        // given
        val userApi = UserApiImpl(
            httpClient = HttpClient(
                MockEngine {
                    respond(
                        content = ByteReadChannel(
                            """
{
    "access_token":"abcdefg",
    "expires_in": 3600,
    "refresh_token":"abcdefg",
    "token_type":"abcdefg"
}
                            """.trimIndent()
                        ),
                        status = HttpStatusCode.PreconditionFailed,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            ) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                        contentType = ContentType.Application.Json
                    )
                }
            },
            userStorage = userStorage,
            settingsRepository = settingsRepository
        )

        // when
        val result = userApi.accessTokenRequest("authCode")

        // then
        Truth.assertThat(result).isEqualTo(true)
    }

    @Test
    fun `get user should return the current user`() = runBlocking {
        // given
        val userApi = UserApiImpl(
            HttpClient(
                MockEngine {
                    respond(
                        content = ByteReadChannel(
                            """
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "email": "info@photos.network",
  "lastname": "Lastname",
  "firstname": "Firstname",
  "lastSeen": "2022-02-22T02:22:22.222Z"
}
                            """.trimIndent()
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            ) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                        contentType = ContentType.Application.Json
                    )
                }
            },
            userStorage = userStorage,
            settingsRepository = settingsRepository
        )

        // when
        val result = userApi.getUser()

        // then
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.id).isEqualTo("3fa85f64-5717-4562-b3fc-2c963f66afa6")
    }
}
