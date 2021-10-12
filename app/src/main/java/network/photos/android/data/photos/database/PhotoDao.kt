package network.photos.android.data.photos.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import network.photos.android.data.photos.database.entities.DatabasePhoto

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos ORDER BY photoId")
    fun getPhotos(): List<DatabasePhoto>

    @Query("DELETE FROM photos")
    fun deleteAllPhotos()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotos(photos: List<DatabasePhoto>)
}
