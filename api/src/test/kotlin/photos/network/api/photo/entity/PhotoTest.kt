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
package photos.network.api.photo.entity

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Test deserializing photo object.
 */
class PhotoTest {
    @Test
    fun testDeserialization() = runBlocking {
        // given
        val jsonString = """
            {
                "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                "name": "filename.ext",
                "owner": "lastname, firstname",
                "date_added": "2023-01-01T01:01:01.130Z",
                "date_taken": "2023-01-01T02:02:02.130Z",
                "image_url": "string"
            }
        """.trimIndent()

        // when
        val response = Json.decodeFromString<Photo>(jsonString)

        // then
        assertEquals(response.id, "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        assertEquals(response.name, "filename.ext")
        assertEquals(response.imageUrl, "string")
        assertEquals(response.dateAdded, "2023-01-01T01:01:01.130Z")
        assertEquals(response.dateTaken, "2023-01-01T02:02:02.130Z")
    }
}
