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
package photos.network.ui.common

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, name = "phone • light", device = Devices.PHONE, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, name = "phone • dark", device = Devices.PHONE, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, name = "foldable", device = Devices.FOLDABLE)
@Preview(showBackground = true, name = "tablet", device = "spec:width=1280dp,height=800dp,dpi=480")
// @Preview(showBackground = true, name = "desktop", device = "spec:width=1920dp,height=1080dp,dpi=480")
annotation class ReferenceDevices
