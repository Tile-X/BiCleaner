package cat.app.bicleaner.util

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
class SharedPreferencesDelegate<T> (
    private val preferences: SharedPreferences,
    private val key: String? = null,
    private val defaultValue: T
) : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val realKey = key ?: property.name
        return when (defaultValue) {
            is Int -> preferences.getInt(realKey, defaultValue)
            is Long -> preferences.getLong(realKey, defaultValue)
            is Float -> preferences.getFloat(realKey, defaultValue)
            is Boolean -> preferences.getBoolean(realKey, defaultValue)
            is String -> preferences.getString(realKey, defaultValue)
            is Set<*> -> preferences.getStringSet(realKey, defaultValue as? Set<String> )
            else -> throw IllegalStateException("Unsupported type")
        } as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val realKey = key ?: property.name
        with (preferences.edit()) {
            when (value) {
                is Int -> putInt(realKey, value)
                is Long -> putLong(realKey, value)
                is Float -> putFloat(realKey, value)
                is Boolean -> putBoolean(realKey, value)
                is String -> putString(realKey, value)
                is Set<*> -> putStringSet(realKey, value as? Set<String> )
                else -> throw IllegalStateException("Unsupported type")
            }
            apply()
        }
    }

}