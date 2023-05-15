/*
 * Copyright 2020-2023 Photos.network developers
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
import photos.network.repository.sharing.UserRepository

class VerifyClientIdUseCaseTests {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val userRepository = mockk<UserRepository>()

    private val verifyClientIdUseCase by lazy {
        VerifyClientIdUseCase(
            userRepository = userRepository,
        )
    }

    @Test
    fun `clientID should not be verified when length less or equal 10`(): Unit = runBlocking {
        // given
        coEvery { userRepository.verifyClientId(any()) } answers { false }

        // when
        verifyClientIdUseCase("1234567890")

        // then
        coVerify(atMost = 0, atLeast = 0) { userRepository.verifyClientId(any()) }
    }

    @Test
    fun `host should be verified via repository when length 8 or more`(): Unit = runBlocking {
        // given
        coEvery { userRepository.verifyClientId(any()) } answers { false }

        // when
        verifyClientIdUseCase("12345678901")

        // then
        coVerify(atMost = 1, atLeast = 1) { userRepository.verifyClientId(any()) }
    }
}
