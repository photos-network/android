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
package photos.network.api.status

import com.google.common.truth.Truth
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.JsonConvertException
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

/**
 * Test status endpoints with static fake data
 */
class StatusApiTests {
    private val settingsStore = mockk<SecureStorage<Settings>>()

    @Before
    fun setup() {
        coEvery { settingsStore.read()?.host } answers { "http://localhost" }
        coEvery { settingsStore.read()?.port } answers { 443 }
        coEvery { settingsStore.read()?.clientId } answers { "TEST-CLIENTID" }
        coEvery { settingsStore.read()?.privacyState } answers { PrivacyState.NONE }
    }

    @Test
    fun `valid status response should succeed`() = runBlocking {
        // given
        val photoApi = StatusApiImpl(
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
        val result = photoApi.getStatus()

        // then
        Truth.assertThat(result.message).isEqualTo("API running.")
    }

    @Test(expected = JsonConvertException::class)
    fun `invalid status response should fail`() = runBlocking {
        // given
        val photoApi = StatusApiImpl(
            httpClient = HttpClient(
                MockEngine {
                    respond(
                        content = ByteReadChannel(
                            """
                            {}
                            """.trimIndent(),
                        ),
                        status = HttpStatusCode.InternalServerError,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                },
            ) {
                install(ContentNegotiation) { json() }
            },
        )

        // when
        val result = photoApi.headStatus()

        // then
        Truth.assertThat(result.message).isEqualTo("")
    }
}
