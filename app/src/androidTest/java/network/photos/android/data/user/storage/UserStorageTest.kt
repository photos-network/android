package network.photos.android.data.user.storage

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [22, 28])
class UserStorageTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val encryptedFile =
        EncryptedFile.Builder(
            File(context.filesDir, "fileToPersist"),
            context,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

    @Test
    fun verify_string_storage() {
        val testString = "StringToTest"

        // write
        encryptedFile.openFileOutput().apply {
            write(testString.toByteArray(StandardCharsets.UTF_8))
            flush()
            close()
        }

        // read
        val inputStream = encryptedFile.openFileInput()
        val byteArrayOutputStream = ByteArrayOutputStream()
        var nextByte: Int = inputStream.read()
        while (nextByte != -1) {
            byteArrayOutputStream.write(nextByte)
            nextByte = inputStream.read()
        }

        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        val decryptedString = String(byteArray, Charsets.UTF_8)

        assertEquals(testString, decryptedString)
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            FakeAndroidKeyStore.setup
        }
    }
}
