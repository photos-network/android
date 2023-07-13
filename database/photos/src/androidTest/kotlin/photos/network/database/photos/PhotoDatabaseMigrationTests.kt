/*
 * Copyright 2020-2023 Photos.network developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package photos.network.database.photos

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

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
        photo.put("imageUrl", "http://127.0.0.1/image/e369d958-ad41-4391-9ccb-f89be8ca1e8b")
        photo.put("dateTaken", 1580671220)
        // dateAdded        added with version 2
        // dateModified     added with version 2
        photo.put("thumbnailFileUri", "http://127.0.0.1/thumb/e369d958-ad41-4391-9ccb-f89be8ca1e8b")
        photo.put("originalFileUri", "http://127.0.0.1/raw/e369d958-ad41-4391-9ccb-f89be8ca1e8b")

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
            TEST_DB,
        ).addMigrations(
            MIGRATION_1_2,
        ).build().apply {
            openHelper.writableDatabase
            close()
        }

        val photos = databaseNew.photoDao().getPhotos().first()
        Truth.assertThat(photos.size).isEqualTo(1)
        Truth.assertThat(photos[0].uuid).isEqualTo("e369d958-ad41-4391-9ccb-f89be8ca1e8b")
        Truth.assertThat(photos[0].filename).isEqualTo("IMG_20200202_202020.jpg")
        Truth.assertThat(photos[0].imageUrl).isEqualTo("http://127.0.0.1/image/e369d958-ad41-4391-9ccb-f89be8ca1e8b")
        Truth.assertThat(photos[0].dateTaken).isEqualTo(1580671220)
        Truth.assertThat(photos[0].dateAdded).isNotNull()
        Truth.assertThat(photos[0].dateModified).isEqualTo(null)
        Truth.assertThat(photos[0].thumbnailFileUri).isEqualTo("http://127.0.0.1/thumb/e369d958-ad41-4391-9ccb-f89be8ca1e8b")
        Truth.assertThat(photos[0].originalFileUri).isEqualTo("http://127.0.0.1/raw/e369d958-ad41-4391-9ccb-f89be8ca1e8b")
    }
}
