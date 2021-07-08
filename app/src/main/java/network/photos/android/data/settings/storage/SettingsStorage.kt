package network.photos.android.data.settings.storage

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
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
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val filename = "settings_storage.txt"
    private val gson: Gson = GsonBuilder().create()
    private val secureFile = File(context.filesDir, filename)

    fun readSettings(): Settings? {
        try {
            val encryptedFile = EncryptedFile.Builder(
                secureFile,
                context.applicationContext,
                masterKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

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
        }

        return null
    }

    fun writeSettings(settings: Settings) {
        val encryptedFile = EncryptedFile.Builder(
            secureFile,
            context.applicationContext,
            masterKeyAlias,
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
}
