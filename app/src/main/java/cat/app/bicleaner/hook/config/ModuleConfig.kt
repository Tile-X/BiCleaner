package cat.app.bicleaner.hook.config

import cat.app.bicleaner.BuildConfig
import cat.app.bicleaner.data.PreferencesData
import cat.app.bicleaner.util.SharedPreferencesHelper
import de.robv.android.xposed.XSharedPreferences
import java.util.stream.Collectors

object ModuleConfig {

    val xsp = XSharedPreferences(BuildConfig.APPLICATION_ID)
    private val data = PreferencesData(SharedPreferencesHelper(xsp))

    val moduleEnable: Boolean
        get() = data.moduleEnable
    val filterByLevel: Boolean
        get() = data.filterByLevel
    val filterByRules: Boolean
        get() = data.filterByRules
    val showFilter: Boolean
        get() = data.showFilter
    val requiredLevel: Int
        get() = data.requiredLevel
    val rules: Set<Regex>
        get() = data.rules.stream().map { Regex(it) }.collect(Collectors.toSet())
}