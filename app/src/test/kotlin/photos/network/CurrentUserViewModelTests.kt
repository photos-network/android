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
package photos.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import photos.network.domain.sharing.usecase.GetCurrentUserUseCase

class CurrentUserViewModelTests {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk<GetCurrentUserUseCase>()
//    private val viewmodel by lazy { CurrentUserViewModel(getCurrentUserUseCase) }

    @Ignore
    @Test
    fun `viewmodel should reflect the given user state from the use case`() = runTest {
        // given
//        every { getCurrentUserUseCase() } answers { flowOf(null) }

        // then
//        Truth.assertThat(viewmodel.currentUser.value).isEqualTo(null)
    }
}
