package network.photos.android.data.photos.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class DatabasePhoto(
    @PrimaryKey val photoId: String,
    val filename: String,
)
