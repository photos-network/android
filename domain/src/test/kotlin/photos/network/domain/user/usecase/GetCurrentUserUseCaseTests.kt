package photos.network.domain.user.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import photos.network.data.user.repository.UserRepository
import photos.network.domain.user.Token
import photos.network.data.user.repository.User

class GetCurrentUserUseCaseTests {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val userRepository = mockk<UserRepository>()

    private val getCurrentUserUseCase by lazy {
        GetCurrentUserUseCase(
            userRepository = userRepository
        )
    }

    @Ignore
    @Test
    fun `use case should return user if available`(): Unit = runBlocking {
        // given
        val user = User(
            id = "f70ce489-f7bf-450b-a75a-295ce8a674c6",
            lastname = "Norris",
            firstname = "Carlos Ray",
            profileImageUrl = "http://localhost/image/chuck_norris.jpg",
            token = "access_token"
        )

//        every { userRepository.currentUser() } answers {
//            flowOf(
//                user
//            )
//        }

        // when
        val result = getCurrentUserUseCase.invoke()

        // then
        Truth.assertThat(result.first()).isEqualTo(user)
    }

    @Ignore
    @Test
    fun `use case should return null if no user is available`(): Unit = runBlocking {
        // given
//        every { userRepository.currentUser() } answers {
//            flowOf(
//                null
//            )
//        }

        // when
        val result = getCurrentUserUseCase.invoke()

        // then
        Truth.assertThat(result.first()).isEqualTo(null)
    }
}
