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
package photos.network.domain.photos.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import org.junit.Rule
import org.junit.Test

class LocationTests {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun `test location dataclass`() {
        // given
        // when
        val location = Location(
            longitude = 180f,
            latitude = 90f,
            altitude = 200
        )
        // then
        Truth.assertThat(location.longitude).isEqualTo(180f)
        Truth.assertThat(location.latitude).isEqualTo(90f)
        Truth.assertThat(location.altitude).isEqualTo(200)
    }
}
