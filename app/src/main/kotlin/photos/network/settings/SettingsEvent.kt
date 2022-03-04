package photos.network.settings

sealed class SettingsEvent {
    object ForceSync: SettingsEvent()
    object EditProfile: SettingsEvent()
    object ToggleActivityLog: SettingsEvent()
    object Login: SettingsEvent()
}
