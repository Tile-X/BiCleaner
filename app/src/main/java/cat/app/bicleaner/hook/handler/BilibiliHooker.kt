package cat.app.bicleaner.hook.handler

import cat.app.bicleaner.hook.filter.BilibiliRepliesFilter
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

class BilibiliHooker: XC_MethodHook(), HookHandler {
    private val scopes = setOf("tv.danmaku.bili", "tv.danmaku.bilibilihd", "com.bilibili.app.in")
    private val filters = listOf(
        BilibiliRepliesFilter()
    )

    override fun matchScope(packageName: String) = scopes.contains(packageName)

    override fun handleHook(classloader: ClassLoader) {
        XposedHelpers.findAndHookMethod(
            "com.bapis.bilibili.main.community.reply.v1.MainListReply",
            classloader,
            "getRepliesList",
            object: XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    super.afterHookedMethod(param)
                    val replies = param.result
                    filters.forEach { filter -> filter.applyFilter(replies) }
                }
            })
    }
}