/*
 * Copyright 2020-2021 Photos.network developers
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
package network.photos.android.data.settings.storage

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE
import androidx.security.crypto.MasterKey.DEFAULT_MASTER_KEY_ALIAS
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import network.photos.android.data.settings.domain.Settings
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets

/**
 * Read/Write settings encrypted into internal storage
 */
class SettingsStorage(private val context: Context) {
    private val filename = "settings_storage.txt"
    private val gson: Gson = GsonBuilder().create()
    private val secureFile = File(context.filesDir, filename)
    private lateinit var masterKey: MasterKey
    private lateinit var encryptedFile: EncryptedFile

    init {
        try {
            initialize(context)
        } catch (e: Exception) {
            Log.e("SettingsStr", "\uD83D\uDC80 EXCEPTION!!", e)
            delete()
            initialize(context)
        }
    }

    private fun initialize(context: Context) {
        val keyGenParameterSpec = KeyGenParameterSpec
            .Builder(
                DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .build()

        masterKey = MasterKey.Builder(context, DEFAULT_MASTER_KEY_ALIAS)
            .setKeyGenParameterSpec(keyGenParameterSpec)
            .build()

        encryptedFile = EncryptedFile.Builder(
            context.applicationContext,
            secureFile,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    fun readSettings(): Settings? {
        try {
            var inputStream: FileInputStream? = null
            try {
                inputStream = encryptedFile.openFileInput()
                val byteArrayOutputStream = ByteArrayOutputStream()
                var nextByte: Int = inputStream.read()
                while (nextByte != -1) {
                    byteArrayOutputStream.write(nextByte)
                    nextByte = inputStream.read()
                }

                inputStream.close()

                val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                val jsonString = String(byteArray, Charsets.UTF_8)

                return gson.fromJson(jsonString, Settings::class.java)
            } catch (e: Exception) {
                // Log exceptions
            } finally {
                inputStream?.close()
            }
        } catch (e: FileNotFoundException) {
            Log.w("SettingsStr", "No previous saved settings found.")
        } catch (e: Exception) {
            Log.e("SettingsStr", "\uD83D\uDC80 EXCEPTION!!", e)
        }

        return null
    }

    fun writeSettings(settings: Settings) {
        val encryptedFile = EncryptedFile.Builder(
            secureFile,
            context.applicationContext,
            DEFAULT_MASTER_KEY_ALIAS,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        if (secureFile.exists()) {
            secureFile.delete()
        }

        val jsonString = gson.toJson(settings)

        encryptedFile.openFileOutput().apply {
            write(jsonString.toByteArray(StandardCharsets.UTF_8))
            flush()
            close()
        }
    }

    fun delete() {
        if (secureFile.exists()) {
            secureFile.delete()
        }
    }
}
