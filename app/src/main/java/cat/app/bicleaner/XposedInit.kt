package cat.app.bicleaner

import android.app.Activity
import android.content.res.Resources
import android.content.res.XModuleResources
import cat.app.bicleaner.hook.BaseHooker
import cat.app.bicleaner.utils.Log
import cat.app.bicleaner.utils.hookBeforeMethod
import cat.app.bicleaner.utils.replaceMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit {

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
        moduleResources = getModuleResources(modulePath)
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        // Hook模块启用状态函数
        if (BuildConfig.APPLICATION_ID == lpparam.packageName) {
            MainActivity.Companion::class.java.name.replaceMethod(
                lpparam.classLoader,
                "isModuleActive"
            ) {
                return@replaceMethod true
            }
            return
        }

        lazyHooker = Activity::class.java.hookBeforeMethod("onResume") {
            callAllLazyHook()
            lazyHooker?.unhook()
        }
    }

    private fun registerHook(hooker: BaseHooker) {
        try {
            hooker.hook()
            hookers.add(hooker)
        } catch (e: Throwable) {
            Log.e(e)
        }
    }

    private fun callAllLazyHook() {
        hookers.forEach {
            try {
                it.lazyHook()
            } catch (e: Throwable) {
                Log.e(e)
            }
        }
    }

    companion object {
        lateinit var modulePath: String
        lateinit var moduleResources: Resources

        private val hookers = ArrayList<BaseHooker>()
        private var lazyHooker: XC_MethodHook.Unhook? = null

        @JvmStatic
        fun getModuleResources(path: String): Resources {
            return XModuleResources.createInstance(path, null)
        }
    }

}