package cat.app.bicleaner.util

import android.annotation.SuppressLint
import android.content.Context

class ModuleUtils {
    companion object {
        fun getModuleVersionName(): String? {
            return null
        }

        fun isModuleActivated(): Boolean {
            return getModuleVersionName() != null
        }

        @SuppressLint("WorldReadableFiles")
        fun getPreferencesMode() = if (isModuleActivated()) Context.MODE_WORLD_READABLE else Context.MODE_PRIVATE
    }
}