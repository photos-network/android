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
package photos.network.api.user

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
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import photos.network.common.persistence.PrivacyState
import photos.network.common.persistence.SecureStorage
import photos.network.common.persistence.Settings
import photos.network.common.persistence.User

/**
 * Test API endpoints for photos with static fake data
 */
class UserApiTests {
    private val settingsStorage = mockk<SecureStorage<Settings>>()
    private val userStorage = mockk<SecureStorage<User>>()

    @Before
    fun setup() {
        coEvery { userStorage.read()?.id } answers { "" }
        coEvery { userStorage.read()?.lastname } answers { "" }
        coEvery { userStorage.read()?.firstname } answers { "" }
        coEvery { userStorage.read()?.profileImageUrl } answers { "" }
        coEvery { userStorage.read()?.accessToken } answers { "" }
        coEvery { userStorage.read()?.refreshToken } answers { "" }

        coEvery { settingsStorage.read()?.host } answers { "http://localhost" }
        coEvery { settingsStorage.read()?.port } answers { 443 }
        coEvery { settingsStorage.read()?.clientId } answers { "TEST-CLIENTID" }
        coEvery { settingsStorage.read()?.privacyState } answers { PrivacyState.NONE }
    }

    @Test
    fun `valid verify server response should return true`() = runBlocking {
        // given
        val userApi = UserApiImpl(
            userStorage = userStorage,
            settingsStorage = settingsStorage,
            httpClient = HttpClient(
                MockEngine {
                    respond(
                        content = ByteReadChannel(
                            """
                            {
                              "message": "API running."
                            }
                            """.trimIndent(),
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                },
            ) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                        contentType = ContentType.Application.Json,
                    )
                }
            },
        )

        // when
        val result = userApi.verifyServerHost("http://localhost")

        // then
        Truth.assertThat(result).isTrue()
    }

    @Test
    fun `invalid verify server response should return false`() = runBlocking {
        // given
        val userApi = UserApiImpl(
            userStorage = userStorage,
            settingsStorage = settingsStorage,
            httpClient = HttpClient(
                MockEngine {
                    respond(
                        content = ByteReadChannel(
                            """
                            Content-Length: 68137
                            Content-Type: multipart/form-data; boundary=---------------------------
                            974767299852498929531610575

                            -----------------------------974767299852498929531610575
                            Content-Disposition: form-data; name="description"
                            """.trimIndent(),
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "text/html; charset=utf-8"),
                    )
                },
            ) {
                install(ContentNegotiation) { json() }
            },
        )

        // when
        val result = userApi.verifyServerHost("http://localhost")

        // then
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `empty verify server response should return false`() = runBlocking {
        // given
        val userApi = UserApiImpl(
            userStorage = userStorage,
            settingsStorage = settingsStorage,
            httpClient = HttpClient(
                MockEngine {
                    respond(
                        content = ByteReadChannel(
                            """
                            """.trimIndent(),
                        ),
                        status = HttpStatusCode.NotFound,
                        headers = headersOf(HttpHeaders.ContentType, "text/plain; charset=utf-8"),
                    )
                },
            ) {
                install(ContentNegotiation) { json() }
            },
        )

        // when
        val result = userApi.verifyServerHost("http://localhost")

        // then
        Truth.assertThat(result).isFalse()
    }
}
