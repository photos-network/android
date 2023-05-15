package photos.network.system.filesystem

import android.app.Application

class FileSystemImpl(
    private val application: Application
): FileSystem {
    override fun getFolders(): List<FolderItem> {
        TODO("Not yet implemented")
    }

    override fun getItems(): List<FileItem> {
        TODO("Not yet implemented")
    }
}
