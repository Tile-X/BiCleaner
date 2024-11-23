package cat.app.bicleaner.utils

import android.annotation.SuppressLint
import android.app.AndroidAppHelper
import android.content.Context
import android.content.pm.PackageManager.GET_META_DATA
import cat.app.bicleaner.XposedInit
import cat.app.bicleaner.property.Bilibili.Companion.instance
import cat.app.bicleaner.property.Constant
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

class Weak<T>(val initializer: () -> T?) {
    private var weakReference: WeakReference<T?>? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = weakReference?.get() ?: let {
        weakReference = WeakReference(initializer())
        weakReference
    }?.get()

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        weakReference = WeakReference(value)
    }
}

val systemContext: Context
    get() {
        val activityThread = "android.app.ActivityThread".findClassOrNull(null)
            ?.callStaticMethod("currentActivityThread")!!
        return activityThread.callMethodAs("getSystemContext")
    }

fun getPackageVersion(packageName: String) = try {
    systemContext.packageManager.getPackageInfo(packageName, 0).run {
        String.format("${packageName}@%s(%s)", versionName, getVersionCode(packageName))
    }
} catch (e: Throwable) {
    Log.e(e)
    "(unknown)"
}


fun getVersionCode(packageName: String) = try {
    @Suppress("DEPRECATION")
    systemContext.packageManager.getPackageInfo(packageName, 0).versionCode
} catch (e: Throwable) {
    Log.e(e)
    null
} ?: 6080000


val appKeyMap = mapOf(
    "tv.danmaku.bili" to "1d8b6e7d45233436",
    "com.bilibili.app.blue" to "07da50c9a0bf829f",
    "com.bilibili.app.in" to "bb3101000e232e27",
    "tv.danmaku.bilibilihd" to "dfca71928277209b",
)

val currentContext by lazy { AndroidAppHelper.currentApplication() as Context }

val packageName: String by lazy { currentContext.packageName }

val isBuiltIn
    get() = XposedInit.modulePath.endsWith("so")

val is64
    get() = currentContext.applicationInfo.nativeLibraryDir.contains("64")

val platform by lazy {
    currentContext.packageManager.getApplicationInfo(packageName, GET_META_DATA).metaData.getString(
        "MOBI_APP"
    )
        ?: when (packageName) {
            Constant.PINK_PACKAGE_NAME -> "android"
            Constant.BLUE_PACKAGE_NAME -> "android_b"
            Constant.PLAY_PACKAGE_NAME -> "android_i"
            Constant.HD_PACKAGE_NAME -> "android_hd"
            else -> "android"
        }
}

@SuppressLint("DiscouragedApi")
fun getId(name: String) = instance.ids[name]
    ?: currentContext.resources.getIdentifier(name, "id", currentContext.packageName)