/*
 * Copyright 2020-2021 Photos.network developers
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
package network.photos.android.data.user.repository

import android.util.Log
import network.photos.android.data.user.domain.Token
import network.photos.android.data.user.domain.User
import network.photos.android.data.user.network.UserApi
import network.photos.android.data.user.storage.UserStorage
import java.util.UUID

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val userStorage: UserStorage
) : UserRepository {
    private var currentUser: User? = null

    override fun currentUser(): User? {
        if (currentUser != null) {
            return currentUser
        }

        currentUser = userStorage.read().takeIf {
            it != null
        }

        return currentUser
    }

    override suspend fun requestAuthorization(authCode: String, clientId: String): Boolean {
        val tokenResponse =
            userApi.accessTokenRequest(authCode, clientId, "http://127.0.0.1:3000/callback")
        Log.i("UserRepo", "accessToken: ${tokenResponse.accessToken}")

        val userDetails = userApi.me("Bearer ${tokenResponse.accessToken}")

        val user = User(
            id = UUID.randomUUID(),
            lastname = userDetails.lastname,
            firstname = userDetails.firstname,
            token = Token(
                accessToken = tokenResponse.accessToken,
                refreshToken = tokenResponse.refreshToken
            )
        )
        currentUser = user
        userStorage.save(user)

        return true
    }

    override suspend fun invalidateAuthorization() {
        Log.i("UserRepo", "delete currently logged in User.")
        currentUser = null
        userStorage.delete()
    }
}
