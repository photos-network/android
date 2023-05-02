package photos.network.ui.photos

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiPhotosModule = module {
    viewModel {
        PhotosViewModel(
            getPhotosUseCase = get(),
            startPhotosSyncUseCase = get(),
        )
    }
}
