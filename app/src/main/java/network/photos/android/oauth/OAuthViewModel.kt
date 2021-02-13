package network.photos.android.oauth

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OAuthViewModel : ViewModel() {
    private val _host = mutableStateOf("")
    val host: MutableState<String>
        get() = _host

    private val _clientId = mutableStateOf("")
    val clientId: MutableState<String>
        get() = _clientId


    private val _clientSecret = mutableStateOf("")
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

    fun requestAccessToken(authCode: String) {
        // TODO: request access token
        Log.e("OAauthViewModel", "request Access token for authCode: $authCode")
    }
}
