package photos.network.domain.folders

import photos.network.repository.folders.FoldersRepository

class GetFoldersUseCase(
    private val foldersRepository: FoldersRepository
) {
    operator fun invoke(): List<String> {
        return listOf("NOT-IMPLEMENTEd")
    }
}
