package network.photos.android.data.photos.database

import androidx.room.Database
import androidx.room.RoomDatabase
import network.photos.android.data.photos.database.entities.DatabasePhoto

@Database(
    entities = [
        DatabasePhoto::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}
