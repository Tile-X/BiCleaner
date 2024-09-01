package cat.app.bicleaner.util

import android.annotation.SuppressLint
import android.content.Context
import java.io.File

class ModuleUtils {
    companion object {
        fun getModuleVersionName(): String? {
            return null
        }

        fun isModuleActivated(): Boolean {
            return getModuleVersionName() != null
        }

        @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
        fun getPreferencesDir(context: Context): File {
            val clazz = Class.forName("android.app.ContextImpl")
            val getImplMethod = clazz.getDeclaredMethod("getImpl", Context::class.java)
            getImplMethod.isAccessible = true;
            val contextImpl = getImplMethod.invoke(null, context)
            val getPrefsDirMethod = clazz.getDeclaredMethod("getPreferencesDir")
            getPrefsDirMethod.isAccessible = true
            val prefsDir = getPrefsDirMethod.invoke(contextImpl)
            return prefsDir as File
        }
    }
}