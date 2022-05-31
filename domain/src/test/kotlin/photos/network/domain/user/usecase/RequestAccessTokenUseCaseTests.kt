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
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import photos.network.data.user.repository.User
import photos.network.data.user.repository.UserRepository

class RequestAccessTokenUseCaseTests {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val userRepository = mockk<UserRepository>()

    private val requestAccessTokenUseCase by lazy {
        RequestAccessTokenUseCase(
            userRepository = userRepository
        )
    }

    @Test
    fun `use case should return user true if access token request was successful`(): Unit = runBlocking {
        // given
        coEvery { userRepository.accessTokenRequest(any()) } answers { true }

        // when
        val result = requestAccessTokenUseCase("abcdefg")

        // then
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result).isEqualTo(true)
    }

    @Test
    fun `use case should return user false if access token request failed`(): Unit = runBlocking {
        // given
        coEvery { userRepository.accessTokenRequest(any()) } answers { false }

        // when
        val result = requestAccessTokenUseCase("gfedcba")

        // then
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result).isEqualTo(false)
    }
}
