package photos.network.home.photos

sealed class PhotosEvent {
    object StartLocalPhotoSyncEvent: PhotosEvent()
}
