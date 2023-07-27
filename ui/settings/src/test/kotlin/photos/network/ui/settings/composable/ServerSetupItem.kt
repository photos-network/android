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
package photos.network.ui.settings.composable

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.InstantAnimationsRule
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test
import photos.network.api.ServerStatus
import photos.network.ui.common.theme.AppTheme

class ServerSetupItem {
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
    fun unavailable_dark() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = true) {
                ServerSetupItem(serverStatus = ServerStatus.UNAVAILABLE)
            }
        }
    }

    @Test
    fun unavailable_light() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = false) {
                ServerSetupItem(serverStatus = ServerStatus.UNAVAILABLE)
            }
        }
    }

    @Test
    fun unavailable_expanded_dark() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = true) {
                ServerSetupItem(
                    serverStatus = ServerStatus.UNAVAILABLE,
                    isExpanded = true,
                )
            }
        }
    }

    @Test
    fun unavailable_expanded_light() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = false) {
                ServerSetupItem(
                    serverStatus = ServerStatus.UNAVAILABLE,
                    isExpanded = true,
                )
            }
        }
    }

    @Test
    fun unavailable_expanded_filled_dark() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = true) {
                ServerSetupItem(
                    serverStatus = ServerStatus.UNAVAILABLE,
                    isExpanded = true,
                    serverHost = "https://demo.photos.network",
                )
            }
        }
    }

    @Test
    fun unavailable_expanded_filled_light() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = false) {
                ServerSetupItem(
                    serverStatus = ServerStatus.UNAVAILABLE,
                    isExpanded = true,
                    serverHost = "https://demo.photos.network",
                )
            }
        }
    }

    @Test
    fun unavailable_expanded_host_filled_and_verified_dark() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = true) {
                ServerSetupItem(
                    serverStatus = ServerStatus.UNAVAILABLE,
                    isExpanded = true,
                    serverHost = "https://demo.photos.network",
                    isHostVerified = true,
                )
            }
        }
    }

    @Test
    fun unavailable_expanded_host_filled_and_verified_light() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = false) {
                ServerSetupItem(
                    serverStatus = ServerStatus.UNAVAILABLE,
                    isExpanded = true,
                    serverHost = "https://demo.photos.network",
                    isHostVerified = true,
                )
            }
        }
    }

    @Test
    fun unavailable_expanded_host_filled_and_verified_clientid_filled_dark() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = true) {
                ServerSetupItem(
                    serverStatus = ServerStatus.UNAVAILABLE,
                    isExpanded = true,
                    serverHost = "https://demo.photos.network",
                    isHostVerified = true,
                    clientId = "ABCDEFG",
                )
            }
        }
    }

    @Test
    fun unavailable_expanded_host_filled_and_verified_clientid_filled_light() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = false) {
                ServerSetupItem(
                    serverStatus = ServerStatus.UNAVAILABLE,
                    isExpanded = true,
                    serverHost = "https://demo.photos.network",
                    isHostVerified = true,
                    clientId = "ABCDEFG",
                )
            }
        }
    }

    @Test
    fun available_dark() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = true) {
                ServerSetupItem(serverStatus = ServerStatus.AVAILABLE)
            }
        }
    }

    @Test
    fun available_light() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = false) {
                ServerSetupItem(serverStatus = ServerStatus.AVAILABLE)
            }
        }
    }
}
