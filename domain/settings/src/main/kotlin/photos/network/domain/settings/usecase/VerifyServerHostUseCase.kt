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

import photos.network.repository.sharing.UserRepository

private const val HOST_MIN_LENGTH = 8

class VerifyServerHostUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(host: String): Boolean {
        return if (host.length > HOST_MIN_LENGTH) {
            userRepository.verifyServerHost(host)
        } else {
            false
        }
    }
}
