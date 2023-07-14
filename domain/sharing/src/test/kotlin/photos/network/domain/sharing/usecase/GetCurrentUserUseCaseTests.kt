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
package photos.network.domain.sharing.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import photos.network.repository.sharing.User
import photos.network.repository.sharing.UserRepository

class GetCurrentUserUseCaseTests {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val userRepository = mockk<UserRepository>()

    private val getCurrentUserUseCase by lazy {
        GetCurrentUserUseCase(
            userRepository = userRepository,
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
            accessToken = "access_token",
        )

//        every { userRepository.currentUser() } answers {
//            UserMapper.mapRepositoryToDatabase(user)
//        }

        // when
        val result = getCurrentUserUseCase().first()

        // then
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.id).isEqualTo(user.id)
        Truth.assertThat(result?.lastname).isEqualTo(user.lastname)
        Truth.assertThat(result?.firstname).isEqualTo(user.firstname)
        Truth.assertThat(result?.profileImageUrl).isEqualTo(user.profileImageUrl)
        Truth.assertThat(result?.accessToken).isEqualTo(user.accessToken)
    }

    @Test
    fun `use case should return null if no user is available`(): Unit = runBlocking {
        // given
        every { userRepository.currentUser() } answers {
            null
        }

        // when
        val result = getCurrentUserUseCase.invoke()

        // then
        Truth.assertThat(result.first()).isEqualTo(null)
    }
}
