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
package photos.network.domain.settings.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import photos.network.data.user.repository.UserRepository
import photos.network.repository.sharing.UserRepository

class VerifyServerHostUseCaseTests {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val userRepository = mockk<UserRepository>()

    private val verifyServerHostUseCase by lazy {
        VerifyServerHostUseCase(
            userRepository = userRepository,
        )
    }

    @Test
    fun `host should not be verified when length less or equal 8`(): Unit = runBlocking {
        // given
        coEvery { userRepository.verifyServerHost(any()) } answers { false }

        // when
        verifyServerHostUseCase("https://")

        // then
        coVerify(atMost = 0, atLeast = 0) { userRepository.verifyServerHost(any()) }
    }

    @Test
    fun `clientId should be verified via repository when length 10 or more`(): Unit = runBlocking {
        // given
        coEvery { userRepository.verifyServerHost(any()) } answers { false }

        // when
        verifyServerHostUseCase("https://l")

        // then
        coVerify(atMost = 1, atLeast = 1) { userRepository.verifyServerHost(any()) }
    }
}
