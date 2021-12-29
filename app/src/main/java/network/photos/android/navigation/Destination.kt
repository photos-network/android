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
package network.photos.android.navigation

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.os.bundleOf
import network.photos.android.R

sealed class Destination(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object Home : Destination("home", R.string.home_title, Icons.Filled.House)
    object Photos : Destination("photos", R.string.photos_title, Icons.Filled.Photo)
    object Details : Destination("details", R.string.details_title, Icons.Filled.Photo)
    object Albums : Destination("albums", R.string.albums_title, Icons.Filled.PhotoAlbum)
    object Login : Destination("login", R.string.login_title, Icons.Filled.Lock)
    object Setup : Destination("setup", R.string.setup_title, Icons.Filled.Settings)
    object Help : Destination("help", R.string.help_title, Icons.Filled.Help)

    fun saveState(): Bundle {
        return bundleOf(KEY_SCREEN to route)
    }

    companion object {
        fun restoreState(bundle: Bundle): Destination {
            val route = bundle.getString(KEY_SCREEN, Home.route)
            return when (route) {
                Home.route -> Home
                Photos.route -> Photos
                Details.route -> Details
                Albums.route -> Albums
                Login.route -> Login
                Setup.route -> Setup
                Help.route -> Help
                else -> Home
            }
        }

        const val KEY_SCREEN = "route"
    }
}
