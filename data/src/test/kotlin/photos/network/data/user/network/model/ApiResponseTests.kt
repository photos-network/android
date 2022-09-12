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
