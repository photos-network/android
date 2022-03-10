package photos.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import photos.network.domain.user.usecase.GetCurrentUserUseCase
import photos.network.user.CurrentUserViewModel

class CurrentUserViewModelTests {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = TestCoroutineDispatcherRule()

    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk<GetCurrentUserUseCase>()
//    private val viewmodel by lazy { CurrentUserViewModel(getCurrentUserUseCase) }

    @Ignore
    @Test
    fun `viewmodel should reflect the given user state from the use case`() {
        // given
//        every { getCurrentUserUseCase() } answers { flowOf(null) }

        // then
//        Truth.assertThat(viewmodel.currentUser.value).isEqualTo(null)
    }
}
