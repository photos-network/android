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
package photos.network.data.user.network.model

import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test

class ApiResponseTests {
    @Test
    fun `parse valid TokenInfo response`() = runBlocking {
        // given
        val jsonString: String = """
{
  "message": "API running"
}
        """.trimIndent()

        // when
        val response = Json.decodeFromString<ApiResponse>(jsonString)

        // then
        Truth.assertThat(response.message).isEqualTo("API running")
    }
}
