package photos.network.home.photos

sealed class PhotosEvent {
    object DeletePhotoEvent: PhotosEvent()
}
