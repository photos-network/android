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
package photos.network.network.user

import photos.network.network.user.model.NetworkUser

interface UserApi {
    /**
     * Verify if the server host is pointing to an Photos.network Core instance.
     *
     * @param host Uri of the configured photos.core instance
     *
     * @return true if the host address can be reached and is returning `API running` within the body.
     */
    suspend fun verifyServerHost(host: String): Boolean

    /**
     * Verify if the client ID is registered and the configured Photos.network Core instance.
     *
     * @param clientId to check against the configured Photos.network instance.
     *
     * @return true if the client ID is valid
     */
    suspend fun verifyClientId(clientId: String): Boolean

    /**
     * Authorization Request based on [RFC6749](https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.3)
     *
     * @param authCode The authorization code returned by the OAuth server after a successful login
     *
     * @return true if the access token could be requested.
     */
    suspend fun accessTokenRequest(authCode: String): Boolean

    /**
     * OAuth token revocation based on [RFC7000](https://tools.ietf.org/html/rfc7009#section-2.1)
     *
     * @param token The token to revoke on the oauth server.
     * @param tokenTypeHint This optional param should either be 'access_token' or 'refresh_token'.
     */
    suspend fun revocationRequest(
        token: String,
        tokenTypeHint: String?,
    )

    suspend fun getUser(): NetworkUser?
}
