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

        fun loadApplicationName(pm: PackageManager, packageName: String): String {
             return getApplicationInfo(pm, packageName)!!.loadLabel(pm).toString()
        }

        fun loadApplicationVersionName(pm: PackageManager, packageName: String): String {
            return getPackageInfo(pm, packageName)!!.versionName
        }

        fun loadApplicationVersionCode(pm: PackageManager, packageName: String): Int {
            return getPackageInfo(pm, packageName)!!.versionCode
        }

        fun loadApplicationIcon(pm: PackageManager, packageName: String): ImageBitmap {
            val application = getApplicationInfo(pm, packageName)
            var icon = Bitmap.createBitmap(192, 192, Bitmap.Config.ALPHA_8)
            if (application != null) {
                icon = (application.loadIcon(pm) as BitmapDrawable).bitmap
            }
            return icon.asImageBitmap()
        }

        fun isApplicationInstalled(pm: PackageManager, packageName: String): Boolean {
            return getPackageInfo(pm, packageName) != null
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