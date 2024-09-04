package cat.app.bicleaner.hook.handler

import cat.app.bicleaner.BuildConfig
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

class ModuleUtilsHooker: HookHandler {
    private val moduleVersionName = BuildConfig.VERSION_NAME;
    private val moduleVersionCode = BuildConfig.VERSION_CODE;
    private val scopes = setOf("cat.app.bicleaner")

    override fun matchScope(packageName: String) = scopes.contains(packageName)

    override fun handleHook(classloader: ClassLoader) {
        XposedHelpers.findAndHookMethod(
            "cat.app.bicleaner.util.ModuleUtils\$Companion",
            classloader,
            "getModuleVersionName",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    param.result = "$moduleVersionName($moduleVersionCode)"
                }
            })
    }
}