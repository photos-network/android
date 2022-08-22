package photos.network.data.user.network.model

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import com.google.common.truth.Truth
import org.junit.Test
import kotlinx.serialization.json.Json

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
