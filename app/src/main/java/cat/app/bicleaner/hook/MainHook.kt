package cat.app.bicleaner.hook

import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook: IXposedHookLoadPackage {

    private val handlers: Array<HookHandler> = arrayOf(
        ModuleUtilsHooker(),
        BilibiliHooker(),
        BilibiliGlobalHooker()
    )

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        handlers.forEach { handler ->
            if (lpparam.packageName == handler.getScopePackage()) {
                handler.handleHook(lpparam.classLoader)
                Log.d("lookcat", "hook: ${lpparam.packageName}")
            }
        }
    }

}