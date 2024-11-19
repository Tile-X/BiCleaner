@file:Suppress("unused")

package cat.app.bicleaner.utils

import android.os.Handler
import android.os.Looper
import de.robv.android.xposed.XposedBridge
import android.util.Log as ALog

object Log {
    private const val TAG = "BiCleaner"
    private const val MAX_LENGTH = 3000

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    @JvmStatic
    private fun doLog(log: (String, String) -> Int, obj: Any?, toXposed: Boolean = false) {
        val str = if (obj is Throwable) ALog.getStackTraceString(obj) else obj.toString()
        if (str.length <= MAX_LENGTH) {
            log(TAG, str)
            if (toXposed) XposedBridge.log("$TAG : $str")
            return
        }
        val chunkCount: Int = str.length / MAX_LENGTH
        for (i in 0..chunkCount) {
            val lineMax: Int = MAX_LENGTH * (i + 1)
            if (lineMax >= str.length) {
                doLog(log, str.substring(MAX_LENGTH * i), toXposed)
            } else {
                doLog(log, str.substring(MAX_LENGTH * i, lineMax), toXposed)
            }
        }
    }

    @JvmStatic
    fun d(obj: Any?) {
        doLog(ALog::d, obj)
    }

    @JvmStatic
    fun i(obj: Any?) {
        doLog(ALog::i, obj)
    }

    @JvmStatic
    fun e(obj: Any?) {
        doLog(ALog::e, obj, true)
    }

    @JvmStatic
    fun v(obj: Any?) {
        doLog(ALog::v, obj)
    }

    @JvmStatic
    fun w(obj: Any?) {
        doLog(ALog::w, obj)
    }
}