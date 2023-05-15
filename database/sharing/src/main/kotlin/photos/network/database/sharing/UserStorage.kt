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
package photos.network.database.sharing

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import photos.network.common.persistence.SecureStorage
import photos.network.common.persistence.User

class UserStorage(context: Context) : SecureStorage<User>(context, "user_storage.txt") {
    override fun decodeData(data: String): User {
        return Json.decodeFromString(data)
    }

    override fun encodeData(data: User): String {
        return Json.encodeToString(data)
    }
}
