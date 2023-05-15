package photos.network.system.filesystem

import org.koin.dsl.module

val systemFilesystemModule = module {
    single<FileSystem> {
        FileSystemImpl(application = get())
    }
}
