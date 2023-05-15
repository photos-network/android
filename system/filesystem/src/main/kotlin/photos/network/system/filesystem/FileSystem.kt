package photos.network.system.filesystem

import android.net.Uri

interface FileSystem {
    fun getFolders(): List<FolderItem>
    fun getItems(): List<FileItem>
}

data class FolderItem(
    val name: String,
    val itemCount: Int,
    val folderSize: Long
)

data class FileItem(
    val name: String,
    val size: Int,
    val uri: Uri,
)
