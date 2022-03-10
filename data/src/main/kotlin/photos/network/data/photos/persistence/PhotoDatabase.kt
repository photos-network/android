/*
 * Copyright 2020-2022 Photos.network developers
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
package photos.network.data.photos.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        Photo::class,
    ],
    version = 2,
    exportSchema = true
)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE `photos_new` (
                `uuid` TEXT, 
                `filename` TEXT PRIMARY KEY NOT NULL, 
                `imageUrl` TEXT NOT NULL,
                `dateTaken` INTEGER,
                `dateAdded` INTEGER NOT NULL,
                `dateModified` INTEGER,
                `thumbnailFileUri` TEXT,
                `originalFileUri` TEXT
            )
            """.trimIndent()
        )

        database.execSQL("""
                INSERT INTO photos_new (uuid, filename, imageUrl, dateTaken, dateAdded, dateModified, thumbnailFileUri, originalFileUri)
                SELECT uuid, filename, imageUrl, dateTaken, strftime('%s', 'now'), NULL, thumbnailFileUri, originalFileUri FROM photos
                """.trimIndent())
        database.execSQL("DROP TABLE photos")
        database.execSQL("ALTER TABLE photos_new RENAME TO photos")
    }
}
