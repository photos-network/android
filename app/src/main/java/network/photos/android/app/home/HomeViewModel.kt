package network.photos.android.app.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import network.photos.android.data.photos.domain.PhotoElement
import network.photos.android.data.settings.domain.Settings
import network.photos.android.data.settings.repository.SettingsRepository
import network.photos.android.data.user.domain.User
import network.photos.android.data.user.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository,
//    private val photosRepository: PhotoRepository,
): ViewModel() {
    val currentUser = mutableStateOf<User?>(null)
    val currentSettings = mutableStateOf<Settings?>(null)

    private val _uiState = MutableStateFlow(HomeUiState(loading = true))
    val uiState: StateFlow<HomeUiState> = _uiState


    init {
        viewModelScope.launch {
            currentSettings.value = settingsRepository.loadSettings()
            currentUser.value = userRepository.currentUser()

//            val photos = photosRepository.getPhotos(0)
//            _uiState.value = HomeUiState(photos = photos)
        }
    }
}

data class HomeUiState(
    val photos: Collection<PhotoElement> = emptyList(),
    val loading: Boolean = false,
    val refreshError: Boolean = false
)
