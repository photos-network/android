package photos.network.account

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AccountViewModel: ViewModel() {
    val uiState = mutableStateOf(AccountUiState())

    fun handleEvent(event: AccountEvent) {
        when (event) {
            else -> {}
        }
    }
}
