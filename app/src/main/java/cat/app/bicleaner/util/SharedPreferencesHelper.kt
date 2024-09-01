package cat.app.bicleaner.util

import android.content.SharedPreferences

class SharedPreferencesHelper(sharedPreferences: SharedPreferences) {

    private val preferences: SharedPreferences = sharedPreferences

    fun <T> getDelegate(key: String?, defaultValue: T): SharedPreferencesDelegate<T> {
        return SharedPreferencesDelegate(preferences, key, defaultValue)
    }

}