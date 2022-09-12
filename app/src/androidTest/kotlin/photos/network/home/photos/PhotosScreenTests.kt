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
package photos.network.home.photos

import androidx.activity.compose.setContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import photos.network.MainActivity
import photos.network.generateTestPhoto
import photos.network.theme.AppTheme

class PhotosScreenTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loading_spinner_should_be_shown_while_loading() {
        // given
        val uiState = PhotosUiState(isLoading = true)

        // when
        composeTestRule.activity.setContent {
            AppTheme {
                PhotosContent(uiState = uiState, handleEvent = {})
            }
        }

        // then
        composeTestRule.onNodeWithTag("LOADING_SPINNER").assertIsDisplayed()
    }

    @Ignore("Broken in test only")
    @Test
    fun back_should_unselect_photo_if_set() {
        // given
        val photo1 = generateTestPhoto(filename = "photo1.jpg")
        val uiState = PhotosUiState(
            photos = listOf(photo1),
            selectedIndex = 1,
            selectedPhoto = photo1,
            isLoading = false,
        )
        var called = false
        val eventHandler: (event: PhotosEvent) -> Unit = {
            if (it is PhotosEvent.SelectIndex) {
                called = true
            }
        }

        // when
        composeTestRule.activity.setContent {
            AppTheme {
                PhotosContent(
                    uiState = uiState,
                    handleEvent = eventHandler
                )
            }
        }
        composeTestRule.activity.onBackPressed()

        // then
        assert(called)
    }

    @Test
    fun swipe_right_should_select_previous_image() {
        // given
        val photo1 = generateTestPhoto(filename = "photo1.jpg")
        val photo2 = generateTestPhoto(filename = "photo2.jpg")
        val uiState = PhotosUiState(
            photos = listOf(photo1, photo2),
            selectedIndex = 1,
            selectedPhoto = photo1,
            isLoading = false,
        )
        var selectedNext = false
        var selectedPrevious = false
        val eventHandler: (event: PhotosEvent) -> Unit = {
            if (it is PhotosEvent.SelectNextPhoto) {
                selectedNext = true
            } else if (it is PhotosEvent.SelectPreviousPhoto) {
                selectedPrevious = true
            }
        }

        // when
        composeTestRule.activity.setContent {
            AppTheme {
                PhotosContent(uiState = uiState, handleEvent = eventHandler)
            }
        }
        composeTestRule.onNodeWithTag("PHOTO_DETAILS").performTouchInput {
            swipe(start = Offset(100f, 100f), end = Offset(800f, 100f))
        }

        // then
        assert(!selectedNext)
        assert(selectedPrevious)
    }
}
