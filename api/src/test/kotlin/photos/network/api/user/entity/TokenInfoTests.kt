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
package photos.network.api.user.entity

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class TokenInfoTests {
    @Suppress("MaxLineLength")
    @Test
    fun testDeserialization() = runBlocking {
        // given
        val jsonString = """
            {
                "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTMyMzM4Mjh9.8U4oXtAHEkYgZldFMduANu-ryhTN5RX69XslPzU7pnQ",
                "token_type": "Bearer",
                "expires_in": 3600,
                "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTMyMzA4ODN9.4kFQD33F7-xQPUWSM9IxsDYqv30zAEa7WS7jpk8NtFU"
            }
        """.trimIndent()

        // when
        val response = Json.decodeFromString<TokenInfo>(jsonString)

        // then
        assertEquals(response.accessToken, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTMyMzM4Mjh9.8U4oXtAHEkYgZldFMduANu-ryhTN5RX69XslPzU7pnQ")
        assertEquals(response.expiresIn, 3600)
        assertEquals(response.refreshToken, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTMyMzA4ODN9.4kFQD33F7-xQPUWSM9IxsDYqv30zAEa7WS7jpk8NtFU")
        assertEquals(response.tokenType, "Bearer")
    }
}
