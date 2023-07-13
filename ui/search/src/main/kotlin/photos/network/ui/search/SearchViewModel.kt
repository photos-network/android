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
package photos.network.ui.search

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val application: Application,
) : ViewModel() {
    val uiState = MutableStateFlow(SearchUiState())

    fun handleEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.UpdateSearchQuery -> updateSearchQuery(event.query)
        }
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.update { it.copy(query = query) }
        }
    }
}
