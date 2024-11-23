package cat.app.bicleaner.property

object Constant {
    const val PINK_PACKAGE_NAME = "tv.danmaku.bili"
    const val BLUE_PACKAGE_NAME = "com.bilibili.app.blue"
    const val PLAY_PACKAGE_NAME = "com.bilibili.app.in"
    const val HD_PACKAGE_NAME = "tv.danmaku.bilibilihd"
    val BILIBILI_PACKAGE_NAME = hashMapOf(
        "原版" to PINK_PACKAGE_NAME,
        "概念版" to BLUE_PACKAGE_NAME,
        "play版" to PLAY_PACKAGE_NAME,
        "HD版" to HD_PACKAGE_NAME
    )

    const val BILIBILI_MAIN_ACTIVITY = "tv.danmaku.bili.MainActivityV2"
    const val HOOK_INFO_JSON = "hook_info.json"
}