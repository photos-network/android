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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkUserTests {
    @Test
    fun testDeserialization() = runBlocking {
        // given
        val jsonString = """
            {
              "id": "c18b3495-e537-46c1-b2e4-ed5ea9bb4c6f",
              "email": "max.mustermann@photos.network",
              "lastname": "Mustermann",
              "firstname": "Max",
              "last_seen": "2023-01-01T01:01:01Z"
            }
        """.trimIndent()

        // when
        val response = Json.decodeFromString<NetworkUser>(jsonString)

        // then
        assertEquals(response.id, "c18b3495-e537-46c1-b2e4-ed5ea9bb4c6f")
        assertEquals(response.email, "max.mustermann@photos.network")
        assertEquals(response.firstname, "Max")
        assertEquals(response.lastname, "Mustermann")
        assertEquals(response.lastSeen, "2023-01-01T01:01:01Z")
    }

    @Suppress("MaxLineLength")
    @Test
    fun testSerialization() = runBlocking {
        // given
        val networkUser = NetworkUser(
            id = "c18b3495-e537-46c1-b2e4-ed5ea9bb4c6f",
            email = "max.mustermann@photos.network",
            firstname = "Max",
            lastname = "Mustermann",
            lastSeen = "2023-01-01T01:01:01Z",
        )

        // when
        val jsonString = Json.encodeToString(networkUser)

        // then
        assertEquals(
            jsonString,
            """{"id":"c18b3495-e537-46c1-b2e4-ed5ea9bb4c6f","email":"max.mustermann@photos.network","lastname":"Mustermann","firstname":"Max","last_seen":"2023-01-01T01:01:01Z"}""".trimIndent(),
        )
    }
}
