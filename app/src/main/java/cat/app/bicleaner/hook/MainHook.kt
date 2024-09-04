package cat.app.bicleaner.hook

import cat.app.bicleaner.hook.handler.BilibiliHooker
import cat.app.bicleaner.hook.handler.HookHandler
import cat.app.bicleaner.hook.handler.ModuleUtilsHooker
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook: IXposedHookLoadPackage {

    private val handlers: Array<HookHandler> = arrayOf(
        ModuleUtilsHooker(),
        BilibiliHooker(),
    )

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        handlers.forEach { handler ->
            if (handler.matchScope(lpparam.packageName)) {
                handler.handleHook(lpparam.classLoader)
                XposedBridge.log("Hook: ${lpparam.packageName} successfully!")
            }
        }
    }

}