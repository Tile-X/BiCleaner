package cat.app.bicleaner.util

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

class PackageUtils {
    companion object {

        private const val BILIBILI_PACKAGE_NAME = "tv.danmaku.bili"

        fun loadBilibiliName(pm: PackageManager): String {
             return getApplicationInfo(pm, BILIBILI_PACKAGE_NAME)!!.loadLabel(pm).toString()
        }

        fun loadBilibiliVersionName(pm: PackageManager): String {
            return getPackageInfo(pm, BILIBILI_PACKAGE_NAME)!!.versionName
        }

        fun loadBilibiliVersionCode(pm: PackageManager): Int {
            return getPackageInfo(pm, BILIBILI_PACKAGE_NAME)!!.versionCode
        }

        fun loadBilibiliIcon(pm: PackageManager): ImageBitmap {
            val bilibili = getApplicationInfo(pm, BILIBILI_PACKAGE_NAME)
            var icon = Bitmap.createBitmap(192, 192, Bitmap.Config.ALPHA_8)
            if (bilibili != null) {
                icon = (bilibili.loadIcon(pm) as BitmapDrawable).bitmap
            }
            return icon.asImageBitmap()
        }

        fun isBilibiliInstalled(pm: PackageManager): Boolean {
            return getPackageInfo(pm, BILIBILI_PACKAGE_NAME) != null
        }

        private fun getApplicationInfo(pm: PackageManager, packageName: String): ApplicationInfo? {
            return getPackageInfo(pm, packageName)?.applicationInfo
        }

        private fun getPackageInfo(pm: PackageManager, packageName: String): PackageInfo? {
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = pm.getPackageInfo(packageName, PackageManager.MATCH_ALL)
            } catch (ignored: Exception) {
            }
            return packageInfo
        }
    }
}