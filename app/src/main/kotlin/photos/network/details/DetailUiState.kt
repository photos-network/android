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
package photos.network.details

import android.content.res.Configuration
import android.net.Uri
import java.time.LocalDateTime

data class DetailUiState(
    // display options
    val orientation: Int = Configuration.ORIENTATION_PORTRAIT,

    // ui elements
    val isLoading: Boolean = true,
    val isImmersive: Boolean = true,

    // content to show
    val uri: Uri? = null,
    val imageUrl: String? = null,

    // privacy
    // favorite

    // details
    val dateTime: LocalDateTime? = null,

    val photoIdentifier: String = "",
    val pixelCount: Float? = null,
    val resolution: Map<Int, Int>? = mapOf(),
    val sizeInBytes: Long? = null,

    val cameraManufacturer: String? = null, // Nikon
    val cameraModel: String? = null, // Z7II
    val colorProfile: String? = null, // sRGB

    val aperture: Float? = null, // f 2.0
    val focalLength: Float? = null, // 50mm
    val exposureTime: Float? = null, // 10000
    val exposureIndex: Float? = null, // ISO 800

    val locationString: String? = null, // Reichstag Berlin
    val location: Map<Long, Long>? = null, // 52.51859, 13.37617  =>  lat: 0 to 90, lon: -180 to +180

    // manual assigned tags by user
    val tags: List<String>? = emptyList(),

    // automatic recognitions
    val recognitions: List<Recognition>? = emptyList(),
)

data class Recognition(
    val label: String,
    val type: RecognitionType = RecognitionType.UNKNOWN,
    val topLeft: Float? = null,
    val bottomRight: Float? = null,
)

enum class RecognitionType {
    UNKNOWN,
    ANIMAL,
    HUMAN,
    TECHNICAL
}
