package cat.app.bicleaner.hook

import android.util.Log
import cat.app.bicleaner.BuildConfig
import cat.app.bicleaner.constant.BiCleanerConstants
import cat.app.bicleaner.data.PreferencesData
import cat.app.bicleaner.hook.util.call
import cat.app.bicleaner.util.FileHelper
import cat.app.bicleaner.util.SharedPreferencesHelper
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedHelpers
import java.util.stream.Collectors

class BilibiliGlobalHooker: HookHandler {
    private val TAG = "lookcat"

    private val xsp = XSharedPreferences(BuildConfig.APPLICATION_ID)
    private val data = PreferencesData(SharedPreferencesHelper(xsp))

    private val rulesFile = FileHelper(xsp.file.parentFile!!, BiCleanerConstants.RULES_FILENAME)

    private val rules: List<Regex> by lazy { rulesFile.reads().stream().map { rule -> Regex(rule) }.collect(
        Collectors.toList()) }

    override fun getScopePackage() = "com.bilibili.app.in"

    override fun handleHook(classloader: ClassLoader) {
        if (!data.moduleEnable) return
        Log.d(TAG, "hook bilibili global")
        XposedHelpers.findAndHookMethod(
            "com.bapis.bilibili.main.community.reply.v1.MainListReply",
            classloader,
            "getRepliesList",
            object: XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    super.afterHookedMethod(param)
                    try {
                        val oldReplies = param.result as List<*>
                        val newReplies = if (data.showFilter) {
                            oldReplies.forEach { reply ->
                                if (!applyFilter(reply)) {
                                    val content = reply.call("getContent")
                                    content.call("setMessage", "该消息已被过滤")
                                }
                            }
                            oldReplies
                        } else {
                            oldReplies.filter { reply -> applyFilter(reply) }
                        }
                        param.result = newReplies
                    } catch (e: Exception) {
                        Log.d(TAG, "error: $e")
                    }
                }
            })
    }

    private fun applyFilter(reply: Any?): Boolean {
        if (data.filterByLevel) {
            val member = reply.call("getMember")
            val name = member.call("getName") as String
            val level = member.call("getLevel") as Long
            if (level < data.requiredLevel) {
                Log.d(TAG, "message filter by level (name: $name, level: $level)")
                return false
            }
        }
        if (data.filterByRules) {
            val content = reply.call("getContent")
            val message = content.call("getMessage") as String
            rules.forEach { rule ->
                if (message.matches(rule)) {
                    Log.d(TAG, "filter by rules (rule: ${rule.pattern})")
                    return false
                }
            }
        }
        return true
    }

}