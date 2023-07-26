package photos.network.ui.settings.composable

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.InstantAnimationsRule
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test
import com.android.ide.common.rendering.api.SessionParams
import photos.network.api.ServerStatus
import photos.network.ui.common.theme.AppTheme

class SettingsHeader {
    @get:Rule
    val instantAnimationsRule = InstantAnimationsRule()

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        showSystemUi = false,
        supportsRtl = true,
        renderingMode = SessionParams.RenderingMode.SHRINK
    )

    @Test
    fun unauthorized_dark() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = true) {
                SettingsHeader(serverStatus = ServerStatus.UNAUTHORIZED)
            }
        }
    }

    @Test
    fun unauthorized_light() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = false) {
                SettingsHeader(serverStatus = ServerStatus.UNAUTHORIZED)
            }
        }
    }

    @Test
    fun unavailable_dark() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = true) {
                SettingsHeader(serverStatus = ServerStatus.UNAVAILABLE)
            }
        }
    }

    @Test
    fun unavailable_light() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = false) {
                SettingsHeader(serverStatus = ServerStatus.UNAVAILABLE)
            }
        }
    }

    @Test
    fun progress_dark() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = true) {
                SettingsHeader(serverStatus = ServerStatus.PROGRESS)
            }
        }
    }

    @Test
    fun progress_light() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = false) {
                SettingsHeader(serverStatus = ServerStatus.PROGRESS)
            }
        }
    }

    @Test
    fun available_dark() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = true) {
                SettingsHeader(serverStatus = ServerStatus.AVAILABLE)
            }
        }
    }

    @Test
    fun available_light() {
        paparazzi.snapshot {
            AppTheme(useDarkTheme = false) {
                SettingsHeader(serverStatus = ServerStatus.AVAILABLE)
            }
        }
    }
}
