package photos.network.data.user.network.model

import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test

class TokenInfoTests {
    @Test
    fun `parse valid TokenInfo response`() = runBlocking {
        // given
        val jsonString: String = """
{
    "access_token":"abcdefg",
    "expires_in": 3600,
    "refresh_token":"gfedcba",
    "token_type":"aabbcc"
}
        """.trimIndent()

        // when
        val tokenInfo = Json.decodeFromString<TokenInfo>(jsonString)

        // then
        Truth.assertThat(tokenInfo.accessToken).isEqualTo("abcdefg")
        Truth.assertThat(tokenInfo.expiresIn).isEqualTo(3600)
        Truth.assertThat(tokenInfo.refreshToken).isEqualTo("gfedcba")
        Truth.assertThat(tokenInfo.tokenType).isEqualTo("aabbcc")
    }
}
