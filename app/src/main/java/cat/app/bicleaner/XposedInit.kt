package cat.app.bicleaner

import android.app.Activity
import android.app.Application
import android.app.Instrumentation
import android.content.Context
import android.content.res.Resources
import android.content.res.XModuleResources
import android.os.Build
import cat.app.bicleaner.hook.BaseHooker
import cat.app.bicleaner.hook.SettingHooker
import cat.app.bicleaner.property.Bilibili
import cat.app.bicleaner.property.Constant
import cat.app.bicleaner.property.Constant.BILIBILI_MAIN_ACTIVITY
import cat.app.bicleaner.utils.Log
import cat.app.bicleaner.utils.findClassOrNull
import cat.app.bicleaner.utils.getPackageVersion
import cat.app.bicleaner.utils.hookBeforeMethod
import cat.app.bicleaner.utils.is64
import cat.app.bicleaner.utils.isBuiltIn
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

        if (!Constant.BILIBILI_PACKAGE_NAME.containsValue(lpparam.packageName) &&
            BILIBILI_MAIN_ACTIVITY.findClassOrNull(lpparam.classLoader) == null
        ) return

        Instrumentation::class.java.hookBeforeMethod(
            "callApplicationOnCreate",
            Application::class.java
        ) { param ->
            if (lpparam.processName.contains(":")) return@hookBeforeMethod
            Log.d("BiliBili process launched ...")
            Log.d("BiCleaner version: ${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE}) from $modulePath${if (isBuiltIn) "(BuiltIn)" else ""}")
            Log.d("Bilibili version: ${getPackageVersion(lpparam.packageName)} (${if (is64) "64" else "32"}bit)")
            Log.d("SDK: ${Build.VERSION.RELEASE}(${Build.VERSION.SDK_INT}); Phone: ${Build.BRAND} ${Build.MODEL}")
            Bilibili(lpparam.classLoader, param.args[0] as Context)
            registerHooker(SettingHooker(lpparam.classLoader))
        }

        lazyHooker = Activity::class.java.hookBeforeMethod("onResume") {
            callLazyHookers()
            lazyHooker?.unhook()
        }
    }

    private fun registerHooker(hooker: BaseHooker) {
        try {
            hooker.hook()
            hookers.add(hooker)
        } catch (e: Throwable) {
            Log.e(e)
        }
    }

    private fun callLazyHookers() {
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