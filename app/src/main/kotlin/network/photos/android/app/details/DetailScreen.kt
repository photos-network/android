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
package network.photos.android.app.details

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberAnimatedNavController(),
    viewModel: DetailViewModel = hiltViewModel(),
    photoIdentifier: String
) {
    // Change configuration to landscape
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Log.v("Details", "orientation: LANDSCAPE")
        }
        Configuration.ORIENTATION_PORTRAIT -> {
            Log.v("Details", "orientation: PORTRAIT")
        }
        Configuration.ORIENTATION_SQUARE -> {
            Log.v("Details", "orientation: SQUARE")
        }
        Configuration.ORIENTATION_UNDEFINED -> {
            Log.v("Details", "orientation: UNDEFINED")
        }
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFFF00FF))
            .wrapContentSize()
            .rotate(90f),
    ) {

        //    // Observe configuration changes
        //    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }
        //
        //    LaunchedEffect(configuration) {
        //        // Save any changes to the orientation value on the configuration object
        //        snapshotFlow { configuration.orientation }
        //            .collect { orientation = it }
        //    }
        //
        //    when (orientation) {
        //        Configuration.ORIENTATION_LANDSCAPE -> {
        //            LandscapeContent()
        //        }
        //        else -> {
        //            PortraitContent()
        //        }
        //    }

        Text(
            modifier = Modifier.fillMaxSize(),
            text = "DetailScreen for $photoIdentifier"
        )
    }
}
