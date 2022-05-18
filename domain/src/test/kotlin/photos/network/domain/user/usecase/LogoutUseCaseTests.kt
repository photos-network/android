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
package photos.network.domain.user.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import photos.network.data.user.repository.User
import photos.network.data.user.repository.UserRepository

class LogoutUseCaseTests {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val userRepository = mockk<UserRepository>()

    private val logoutUseCase by lazy {
        LogoutUseCase(
            userRepository = userRepository
        )
    }

    @Test
    fun `use case should trigger repository for token invalidation`(): Unit = runBlocking {
        // given
        coEvery { userRepository.invalidateAuthorization() } answers {}

        // when
        logoutUseCase()

        // then
        coVerify(atLeast = 1, atMost = 1) { userRepository.invalidateAuthorization() }
    }
}
