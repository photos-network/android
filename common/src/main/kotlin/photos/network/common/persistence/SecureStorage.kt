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
package photos.network.common.persistence

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets

@Suppress("TooGenericExceptionCaught", "SwallowedException")
abstract class SecureStorage<T>(
    context: Context,
    filename: String,
) {
    private val secureFile = File(context.filesDir, filename)
    private lateinit var masterKey: MasterKey
    protected lateinit var encryptedFile: EncryptedFile

    init {
        try {
            initialize(context)
        } catch (e: Exception) {
            delete()
            initialize(context)
        }
    }

    abstract fun decodeData(data: String): T

    abstract fun encodeData(data: T): String

    @Suppress("SwallowedException")
    open fun read(): T? {
        try {
            val inputStream = encryptedFile.openFileInput()
            val byteArrayOutputStream = ByteArrayOutputStream()
            var nextByte: Int = inputStream.read()
            while (nextByte != -1) {
                byteArrayOutputStream.write(nextByte)
                nextByte = inputStream.read()
            }

            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            val jsonString = String(byteArray, Charsets.UTF_8)

            return decodeData(jsonString)
        } catch (e: Exception) {
            // handle exception
        }

        return null
    }

    open fun save(data: T) {
        val jsonString = encodeData(data)

        delete()

        encryptedFile.openFileOutput().apply {
            write(jsonString.toByteArray(StandardCharsets.UTF_8))
            flush()
            close()
        }
    }

    private fun initialize(context: Context) {
        val keyGenParameterSpec = KeyGenParameterSpec
            .Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
            )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .build()

        masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyGenParameterSpec(keyGenParameterSpec)
            .build()

        encryptedFile = EncryptedFile.Builder(
            context.applicationContext,
            secureFile,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB,
        ).build()
    }

    fun delete() {
        if (secureFile.exists()) {
            secureFile.deleteRecursively()
        }
    }
}
