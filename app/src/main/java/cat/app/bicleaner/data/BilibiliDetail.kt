package cat.app.bicleaner.data

import android.content.pm.PackageManager
import androidx.compose.ui.graphics.ImageBitmap
import cat.app.bicleaner.util.PackageUtils

class BilibiliDetail(packageManager: PackageManager) {

    private val pm = packageManager

    val isBilibiliInstalled: Boolean
        get() = PackageUtils.isBilibiliInstalled(pm)

    val bilibiliIcon: ImageBitmap
        get() = PackageUtils.loadBilibiliIcon(pm)

    val bilibiliName: String
        get() = PackageUtils.loadBilibiliName(pm)

    val bilibiliVersionName: String
        get() = PackageUtils.loadBilibiliVersionName(pm)

    val bilibiliVersionCode: Int
        get() = PackageUtils.loadBilibiliVersionCode(pm)

}