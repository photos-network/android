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
package photos.network.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

val colorLightPrimary = Color(0xFF0062a2)
val colorLightOnPrimary = Color(0xFFffffff)
val colorLightPrimaryContainer = Color(0xFFcfe4ff)
val colorLightOnPrimaryContainer = Color(0xFF001d36)
val colorLightSecondary = Color(0xFF5056a9)
val colorLightOnSecondary = Color(0xFFffffff)
val colorLightSecondaryContainer = Color(0xFFdfe0ff)
val colorLightOnSecondaryContainer = Color(0xFF050764)
val colorLightTertiary = Color(0xFF0061a3)
val colorLightOnTertiary = Color(0xFFffffff)
val colorLightTertiaryContainer = Color(0xFFcfe4ff)
val colorLightOnTertiaryContainer = Color(0xFF001d36)
val colorLightError = Color(0xFFB3261E)
val colorLightErrorContainer = Color(0xFFF9DEDC)
val colorLightOnError = Color(0xFFFFFFFF)
val colorLightOnErrorContainer = Color(0xFF410E0B)
val colorLightBackground = Color(0xFFFFFBFE)
val colorLightOnBackground = Color(0xFF1C1B1F)
val colorLightSurface = Color(0xFFFFFBFE)
val colorLightOnSurface = Color(0xFF1C1B1F)
val colorLightSurfaceVariant = Color(0xFFE7E0EC)
val colorLightOnSurfaceVariant = Color(0xFF49454F)
val colorLightOutline = Color(0xFF79747E)
val colorLightInverseOnSurface = Color(0xFFF4EFF4)
val colorLightInverseSurface = Color(0xFF313033)
val colorLightInversePrimary = Color(0xFF9acaff)
val colorLightShadow = Color(0xFF000000)

val colorDarkPrimary = Color(0xFF9acaff)
val colorDarkOnPrimary = Color(0xFF003258)
val colorDarkPrimaryContainer = Color(0xFF00497c)
val colorDarkOnPrimaryContainer = Color(0xFFcfe4ff)
val colorDarkSecondary = Color(0xFFbdc2ff)
val colorDarkOnSecondary = Color(0xFF202578)
val colorDarkSecondaryContainer = Color(0xFF383e90)
val colorDarkOnSecondaryContainer = Color(0xFFdfe0ff)
val colorDarkTertiary = Color(0xFF9acaff)
val colorDarkOnTertiary = Color(0xFF003259)
val colorDarkTertiaryContainer = Color(0xFF00497d)
val colorDarkOnTertiaryContainer = Color(0xFFcfe4ff)
val colorDarkError = Color(0xFFF2B8B5)
val colorDarkErrorContainer = Color(0xFF8C1D18)
val colorDarkOnError = Color(0xFF601410)
val colorDarkOnErrorContainer = Color(0xFFF9DEDC)
val colorDarkBackground = Color(0xFF1C1B1F)
val colorDarkOnBackground = Color(0xFFE6E1E5)
val colorDarkSurface = Color(0xFF1C1B1F)
val colorDarkOnSurface = Color(0xFFE6E1E5)
val colorDarkSurfaceVariant = Color(0xFF49454F)
val colorDarkOnSurfaceVariant = Color(0xFFCAC4D0)
val colorDarkOutline = Color(0xFF938F99)
val colorDarkInverseOnSurface = Color(0xFF1C1B1F)
val colorDarkInverseSurface = Color(0xFFE6E1E5)
val colorDarkInversePrimary = Color(0xFF0062a2)
val colorDarkShadow = Color(0xFF000000)

val seed = Color(0xFF6750A4)
val error = Color(0xFFB3261E)

internal val lightColors = lightColorScheme(
    primary = colorLightPrimary,
    onPrimary = colorLightOnPrimary,
    primaryContainer = colorLightPrimaryContainer,
    onPrimaryContainer = colorLightOnPrimaryContainer,
    secondary = colorLightSecondary,
    onSecondary = colorLightOnSecondary,
    secondaryContainer = colorLightSecondaryContainer,
    onSecondaryContainer = colorLightOnSecondaryContainer,
    tertiary = colorLightTertiary,
    onTertiary = colorLightOnTertiary,
    tertiaryContainer = colorLightTertiaryContainer,
    onTertiaryContainer = colorLightOnTertiaryContainer,
    error = colorLightError,
    errorContainer = colorLightErrorContainer,
    onError = colorLightOnError,
    onErrorContainer = colorLightOnErrorContainer,
    background = colorLightBackground,
    onBackground = colorLightOnBackground,
    surface = colorLightSurface,
    onSurface = colorLightOnSurface,
    surfaceVariant = colorLightSurfaceVariant,
    onSurfaceVariant = colorLightOnSurfaceVariant,
    outline = colorLightOutline,
    inverseOnSurface = colorLightInverseOnSurface,
    inverseSurface = colorLightInverseSurface,
    inversePrimary = colorLightInversePrimary,
)

internal val darkColors = darkColorScheme(
    primary = colorDarkPrimary,
    onPrimary = colorDarkOnPrimary,
    primaryContainer = colorDarkPrimaryContainer,
    onPrimaryContainer = colorDarkOnPrimaryContainer,
    secondary = colorDarkSecondary,
    onSecondary = colorDarkOnSecondary,
    secondaryContainer = colorDarkSecondaryContainer,
    onSecondaryContainer = colorDarkOnSecondaryContainer,
    tertiary = colorDarkTertiary,
    onTertiary = colorDarkOnTertiary,
    tertiaryContainer = colorDarkTertiaryContainer,
    onTertiaryContainer = colorDarkOnTertiaryContainer,
    error = colorDarkError,
    errorContainer = colorDarkErrorContainer,
    onError = colorDarkOnError,
    onErrorContainer = colorDarkOnErrorContainer,
    background = colorDarkBackground,
    onBackground = colorDarkOnBackground,
    surface = colorDarkSurface,
    onSurface = colorDarkOnSurface,
    surfaceVariant = colorDarkSurfaceVariant,
    onSurfaceVariant = colorDarkOnSurfaceVariant,
    outline = colorDarkOutline,
    inverseOnSurface = colorDarkInverseOnSurface,
    inverseSurface = colorDarkInverseSurface,
    inversePrimary = colorDarkInversePrimary,
)
