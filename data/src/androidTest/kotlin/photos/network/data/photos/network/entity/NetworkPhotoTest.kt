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
package photos.network.data.photos.network.entity

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import photos.network.data.PhotosNetworkMockFileReader

/**
 * Test deserializing photo object.
 */
@RunWith(AndroidJUnit4::class)
class NetworkPhotoTest {

    @Ignore("Not fully implemented")
    @Test
    fun testDeserialization() = runBlocking {
        // given
        val jsonString = PhotosNetworkMockFileReader.readStringFromFile("photo_object.json")

        // when
        val response = Json.decodeFromString<NetworkPhoto>(jsonString)

        // then
        assertEquals(response.id, "photoIdentifier")
        assertEquals(response.name, "photoName")
        assertEquals(response.imageUrl, "")
        assertEquals(response.dateAdded, "")
        assertEquals(response.dateTaken, "")
    }
}
