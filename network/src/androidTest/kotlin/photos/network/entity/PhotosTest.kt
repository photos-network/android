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
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import photos.network.common.PhotosNetworkMockFileReader
import photos.network.network.photo.Photo
import photos.network.network.photo.Photos

/**
 * Test (de)serializing photos responses.
 */
@RunWith(AndroidJUnit4::class)
class PhotosTest {

    @Test
    fun testDeserialization() = runBlocking {
        // given
        val jsonString =
            PhotosNetworkMockFileReader.readStringFromFile("photos_response_success.json")

        // when
        val response = Json.decodeFromString<Photos>(jsonString)

        // then
        Assert.assertEquals(13, response.offset)
        Assert.assertEquals(25, response.limit)
        Assert.assertEquals(2, response.size)
        Assert.assertEquals(
            listOf(
                Photo("a1", "xy", "https://photos.network/foo.raw", null, null),
                Photo("b2", "yz", "https://photos.network/bar.raw", null, null),
            ),
            response.results,
        )
    }
//
//    @Test
//    fun testSerialization() = runBlocking {
//        // given
//        val input = NetworkPhotos(
//            13, 25, 2,
//            listOf(
//                NetworkPhoto("a1", "xy", "", "", ""),
//                NetworkPhoto("b2", "yz", "", "", ""),
//            )
//        )
//
//        // when
//        val jsonElement = Json.encodeToJsonElement(input)
//
//        // then
//        assert(jsonElement.jsonObject["id"].toString() == "a1")
//        assert(jsonElement.jsonObject["name"].toString() == "xy")
//    }
}
