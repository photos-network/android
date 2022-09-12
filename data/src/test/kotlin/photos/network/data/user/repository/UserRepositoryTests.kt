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
package photos.network.data.user.repository

import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import photos.network.data.TestCoroutineDispatcherRule
import photos.network.data.user.network.UserApi
import photos.network.data.user.network.model.NetworkUser
import photos.network.data.user.persistence.UserStorage
import photos.network.data.user.persistence.User as DatabaseUser

class UserRepositoryTests {
    @get:Rule
    val coroutineRule = TestCoroutineDispatcherRule()

    private val userStorage = mockk<UserStorage>()

    private val userApi = mockk<UserApi>()

    private val userRepository by lazy {
        UserRepositoryImpl(
            userApi = userApi,
            userStorage = userStorage
        )
    }

    @Test
    fun `should reflect user from persistence`() = runBlocking {
        // given
        every { userStorage.read() } answers { fakeUser() }
        every { userStorage.save(any()) } answers { Unit }
        coEvery { userApi.getUser() } answers { fakeNetworkUser() }

        // when
        val user = userRepository.currentUser()

        // then
        Truth.assertThat(user).isNotNull()
    }

    private fun fakeUser(
        id: String = "123-abc-456-789",
        lastname: String = "Done",
        firstname: String = "Jane",
        profileImageUrl: String = "http://localhost/foo/bar/jane.jpg",
        accessToken: String? = null,
        refreshToken: String? = null,
    ): DatabaseUser = DatabaseUser(
        id = id,
        lastname = lastname,
        firstname = firstname,
        profileImageUrl = profileImageUrl,
        accessToken = accessToken,
        refreshToken = refreshToken,
    )

    private fun fakeNetworkUser(
        id: String = "123-abc-456-789",
        email: String = "Done",
        lastname: String = "Jane",
        firstname: String = "http://localhost/foo/bar/jane.jpg",
        lastSeen: String = "",
    ): NetworkUser = NetworkUser(
        id = id,
        email = email,
        lastname = lastname,
        firstname = firstname,
        lastSeen = lastSeen,
    )
}
