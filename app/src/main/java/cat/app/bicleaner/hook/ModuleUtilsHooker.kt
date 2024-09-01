package cat.app.bicleaner.hook

import cat.app.bicleaner.BuildConfig
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

class ModuleUtilsHooker: XC_MethodHook(), HookHandler {

    private val moduleVersionName = BuildConfig.VERSION_NAME;
    private val moduleVersionCode = BuildConfig.VERSION_CODE;

    override fun getScopePackage() = "cat.app.bicleaner"

    override fun handleHook(classloader: ClassLoader) {
        XposedHelpers.findAndHookMethod(
            "cat.app.bicleaner.util.ModuleUtils\$Companion",
            classloader,
            "getModuleVersionName",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    super.afterHookedMethod(param)
                    param.result = "$moduleVersionName($moduleVersionCode)"
                }
            })
    }
}