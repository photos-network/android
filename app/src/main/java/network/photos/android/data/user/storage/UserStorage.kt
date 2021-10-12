package network.photos.android.data.user.storage

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import network.photos.android.data.user.domain.User
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets

class UserStorage(context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val filename = "user_storage.txt"
    private val gson: Gson = GsonBuilder().create()
    private val secureFile = File(context.filesDir, filename)
    private val encryptedFile = EncryptedFile.Builder(
        secureFile,
        context.applicationContext,
        masterKeyAlias,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()

    fun save(user: User) {
        val jsonString = gson.toJson(user)

        delete()

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

    fun read(): User? {
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

            return gson.fromJson(jsonString, User::class.java)
        } catch (e: Exception) {
            // Log exceptions
        }

        return null
    }
}
