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
package photos.network.navigation

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.os.bundleOf
import photos.network.R

/**
 * Navigation destinations with titles and icons
 */
sealed class Destination(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object Home : Destination("home", R.string.home_title, Icons.Filled.House)
    object Photos : Destination("photos", R.string.photos_title, Icons.Filled.Photo)
    object Albums : Destination("albums", R.string.albums_title, Icons.Filled.PhotoAlbum)
    object Account : Destination("account", R.string.account_title, Icons.Filled.People)
    object Folders : Destination("folders", R.string.folders_title, Icons.Filled.Folder)
    object Details : Destination("details", R.string.details_title, Icons.Filled.Photo)
    object Login : Destination("login", R.string.login_title, Icons.Filled.Lock)
    object Setup : Destination("setup", R.string.setup_title, Icons.Filled.Settings)
    object Help : Destination("help", R.string.help_title, Icons.Filled.Help)

    fun isRootDestination(): Boolean {
        return this == Photos || this == Albums || this == Folders
    }

    fun saveState(): Bundle {
        return bundleOf(KEY_SCREEN to route)
    }

    companion object {
        fun fromString(route: String?): Destination {
            return when (route) {
                Home.route -> Home
                Photos.route -> Photos
                Albums.route -> Albums
                Account.route -> Account
                Folders.route -> Folders
                Details.route -> Details
                Login.route -> Login
                Setup.route -> Setup
                Help.route -> Help
                else -> Home
            }
        }

        const val KEY_SCREEN = "route"
    }
}
