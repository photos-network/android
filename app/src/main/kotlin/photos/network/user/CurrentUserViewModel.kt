package photos.network.user

import androidx.lifecycle.ViewModel
import photos.network.data.user.repository.UserRepository
import photos.network.domain.user.User

class CurrentUserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    val currentUser: User?
        get() {
            return userRepository.currentUser()?.let {
                User(
                    lastname = it.lastname,
                    firstname = it.firstname,
                    profileImageUrl = it.profileImageUrl,
                )
            }
        }
}
