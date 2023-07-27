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
package photos.network.ui.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.InstantAnimationsRule
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test
import photos.network.ui.common.theme.AppTheme

class FormInput {
    @get:Rule
    val instantAnimationsRule = InstantAnimationsRule()

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        showSystemUi = false,
        supportsRtl = true,
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun with_icon_dark() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = true) {
                FormInput(
                    label = "Label",
                    value = "Content",
                    trailingIcon = Icons.Default.Check,
                )
            }
        }
    }

    @Test
    fun with_icon_light() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = false) {
                FormInput(
                    label = "Label",
                    value = "Content",
                    trailingIcon = Icons.Default.Check,
                )
            }
        }
    }
}
