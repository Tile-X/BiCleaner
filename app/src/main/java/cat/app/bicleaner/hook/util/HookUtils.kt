package cat.app.bicleaner.hook.util

import de.robv.android.xposed.XposedHelpers

fun Any?.call(methodName: String, vararg args: Any): Any? {
    if (this == null) return null
    return XposedHelpers.callMethod(this, methodName, *args)
}

fun Any?.get(fieldName: String): Any? {
    if (this == null) return null
    return XposedHelpers.getObjectField(this, fieldName)
}

fun Any?.set(fieldName: String, value: Any?) {
    if (this == null) return
    XposedHelpers.setObjectField(this, fieldName, value)
}