package photos.network

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

fun Context.versionString(): String {
    val pm: PackageManager = packageManager
    val pkgName: String = packageName
    val pkgInfo: PackageInfo? = try {
        pm.getPackageInfo(pkgName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        null
    }
    val versionName = pkgInfo?.versionName ?: "Unknown"
    val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        pkgInfo?.longVersionCode ?: "0"
    } else {
        pkgInfo?.versionCode ?: "0"
    }

    val versionText = if (isDebuggable()) {
        "$versionName ($versionCode)"
    } else {
        versionName
    }

    return versionText
}

fun Context.isDebuggable(): Boolean {
    return (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
}
