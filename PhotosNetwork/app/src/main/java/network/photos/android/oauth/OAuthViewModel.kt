package network.photos.android.oauth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OAuthViewModel : ViewModel() {
    private val _host = mutableStateOf("https://slack.stuermer.pro")
    val host: MutableState<String>
        get() = _host

    private val _clientId = mutableStateOf("1803463f-c10f-4a65-aa15-b2e39be9f14d")
    val clientId: MutableState<String>
        get() = _clientId


    private val _clientSecret = mutableStateOf("1TmlFYywRd7MwlbRNiePjQ")
    val clientSecret: MutableState<String>
        get() = _clientSecret

    private val _isConnectionCheckInProgress = mutableStateOf(false)
    val isConnectionCheckInProgress: MutableState<Boolean>
        get() = _isConnectionCheckInProgress

    private val _isConnectionValid = mutableStateOf(false)
    val isConnectionValid: MutableState<Boolean>
        get() = _isConnectionValid

    fun checkConnection() {
        viewModelScope.launch {
            _isConnectionCheckInProgress.value = true

            delay(3_000)

            // TODO: if connection was successfull
            _isConnectionValid.value = true
            _isConnectionCheckInProgress.value = false
        }
    }
}
