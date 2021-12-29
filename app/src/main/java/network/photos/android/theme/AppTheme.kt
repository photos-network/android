/*
 * Copyright 2020-2021 Photos.network developers
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
package network.photos.android.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

/**
 * Custom application theme based on a post of @DmytroShuba
 *
 */
object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val dimensions: AppDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current

    val shapes: AppShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current
}

@Composable
fun AppTheme(
    colors: AppColors = AppTheme.colors,
    typography: AppTypography = AppTheme.typography,
    dimensions: AppDimensions = AppTheme.dimensions,
    shapes: AppShapes = AppTheme.shapes,
    content: @Composable () -> Unit
) {
    // Explicitly creating a new object here so we don't mutate the initial [colors]
    // provided, and overwrite the values set in it.
    val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }
    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalDimensions provides dimensions,
        LocalTypography provides typography,
        LocalShapes provides shapes,
    ) {
        content()
    }
}
