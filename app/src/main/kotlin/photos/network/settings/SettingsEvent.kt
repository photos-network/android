package photos.network.settings

sealed class SettingsEvent {
    class HostChanged(val newHost: String): SettingsEvent()
    class ClientIdChanged(val newId: String): SettingsEvent()
    class ClientSecretChanged(val newSecret: String): SettingsEvent()
    object ForceSync: SettingsEvent()
    object EditProfile: SettingsEvent()
    object ToggleActivityLog: SettingsEvent()
    object Login: SettingsEvent()
}
