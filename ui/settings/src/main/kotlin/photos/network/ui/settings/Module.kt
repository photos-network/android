package photos.network.ui.settings

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiSettingsModule = module {
    viewModel {
        SettingsViewModel(
            application = get(),
            getSettingsUseCase = get(),
            updateHostUseCase = get(),
            updateClientIdUseCase = get(),
            verifyServerHostUseCase = get(),
            verifyClientIdUseCase = get(),
        )
    }
}
