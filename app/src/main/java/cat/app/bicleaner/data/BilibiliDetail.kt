package cat.app.bicleaner.data

import android.content.pm.PackageManager
import androidx.compose.ui.graphics.ImageBitmap
import cat.app.bicleaner.constant.BiCleanerConstants.Companion.BILIBILI_GLOBAL_PACKAGE_NAME
import cat.app.bicleaner.constant.BiCleanerConstants.Companion.BILIBILI_HD_PACKAGE_NAME
import cat.app.bicleaner.constant.BiCleanerConstants.Companion.BILIBILI_PACKAGE_NAME
import cat.app.bicleaner.util.PackageUtils

class BilibiliDetail(packageManager: PackageManager) {

    private val pm = packageManager

    // Bilibili 原版
    val isBilibiliInstalled: Boolean
        get() = PackageUtils.isApplicationInstalled(pm, BILIBILI_PACKAGE_NAME)

    val bilibiliIcon: ImageBitmap
        get() = PackageUtils.loadApplicationIcon(pm, BILIBILI_PACKAGE_NAME)

    val bilibiliName: String
        get() = PackageUtils.loadApplicationName(pm, BILIBILI_PACKAGE_NAME)

    val bilibiliVersionName: String
        get() = PackageUtils.loadApplicationVersionName(pm, BILIBILI_PACKAGE_NAME)

    val bilibiliVersionCode: Int
        get() = PackageUtils.loadApplicationVersionCode(pm, BILIBILI_PACKAGE_NAME)

    // Bilibili HD版
    val isBilibiliHDInstalled: Boolean
        get() = PackageUtils.isApplicationInstalled(pm, BILIBILI_HD_PACKAGE_NAME)

    val bilibiliHDIcon: ImageBitmap
        get() = PackageUtils.loadApplicationIcon(pm, BILIBILI_HD_PACKAGE_NAME)

    val bilibiliHDName: String
        get() = PackageUtils.loadApplicationName(pm, BILIBILI_HD_PACKAGE_NAME)

    val bilibiliHDVersionName: String
        get() = PackageUtils.loadApplicationVersionName(pm, BILIBILI_HD_PACKAGE_NAME)

    val bilibiliHDVersionCode: Int
        get() = PackageUtils.loadApplicationVersionCode(pm, BILIBILI_HD_PACKAGE_NAME)

    // Bilibili 国际版
    val isBilibiliGlobalInstalled: Boolean
        get() = PackageUtils.isApplicationInstalled(pm, BILIBILI_GLOBAL_PACKAGE_NAME)

    val bilibiliGlobalIcon: ImageBitmap
        get() = PackageUtils.loadApplicationIcon(pm, BILIBILI_GLOBAL_PACKAGE_NAME)

    val bilibiliGlobalName: String
        get() = PackageUtils.loadApplicationName(pm, BILIBILI_GLOBAL_PACKAGE_NAME)

    val bilibiliGlobalVersionName: String
        get() = PackageUtils.loadApplicationVersionName(pm, BILIBILI_GLOBAL_PACKAGE_NAME)

    val bilibiliGlobalVersionCode: Int
        get() = PackageUtils.loadApplicationVersionCode(pm, BILIBILI_GLOBAL_PACKAGE_NAME)

}