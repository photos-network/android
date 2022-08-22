package photos.network.data.user.network.model

import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test

class NetworkUserTests {
    @Test
    fun `parse valid network user response`() = runBlocking {
        // given
        val jsonString: String = """
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "email": "info@photos.network",
  "lastname": "Lastname",
  "firstname": "Firstname",
  "lastSeen": "2022-02-22T02:22:22.222Z"
}
        """.trimIndent()

        // when
        val networkUser = Json.decodeFromString<NetworkUser>(jsonString)

        // then
        Truth.assertThat(networkUser.id).isEqualTo("3fa85f64-5717-4562-b3fc-2c963f66afa6")
        Truth.assertThat(networkUser.email).isEqualTo("info@photos.network")
        Truth.assertThat(networkUser.lastname).isEqualTo("Lastname")
        Truth.assertThat(networkUser.firstname).isEqualTo("Firstname")
        Truth.assertThat(networkUser.lastSeen).isEqualTo("2022-02-22T02:22:22.222Z")
    }
}
