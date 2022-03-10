package photos.network.data.photos.persistence

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import java.io.IOException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotoDatabaseMigrationTests {
    companion object {
        private const val TEST_DB = "migration-test"
    }

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        PhotoDatabase::class.java,
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() = runBlocking {
        // given
        val photo = ContentValues()
        photo.put("uuid", "e369d958-ad41-4391-9ccb-f89be8ca1e8b")
        photo.put("filename", "IMG_20200202_202020.jpg")
        photo.put("imageUrl", "")
        photo.put("dateTaken", 1580671220)
//        photo.put("dateAdded", 1580671220)
//        photo.put("dateModified", 1580671220)
        photo.put("thumbnailFileUri", "NULL")
        photo.put("originalFileUri", "NUL")

        helper.createDatabase(TEST_DB, 1).apply {
            insert("photos", SQLiteDatabase.CONFLICT_REPLACE, photo)
            close()
        }

        // when
        helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

        // then
        val databaseNew = Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            PhotoDatabase::class.java,
            TEST_DB
        ).addMigrations(
            MIGRATION_1_2
        ).build().apply {
            openHelper.writableDatabase
            close()
        }

        val photos = databaseNew.photoDao().getPhotos().first()
        Truth.assertThat(photos.size).isEqualTo(1)
        Truth.assertThat(photos[0].uuid).isEqualTo("e369d958-ad41-4391-9ccb-f89be8ca1e8b")
    }
}
