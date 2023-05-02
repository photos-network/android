package photos.network.ui.sharing

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import photos.network.ui.sharing.login.LoginViewModel

val uiSharingModule = module {
    viewModel {
        LoginViewModel(
            requestAccessTokenUseCase = get(),
            settingsUseCase = get(),
        )
    }
}
