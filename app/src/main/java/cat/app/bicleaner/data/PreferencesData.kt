package cat.app.bicleaner.data

import cat.app.bicleaner.constant.BiCleanerConstants.Companion.FILTER_BY_LEVEL
import cat.app.bicleaner.constant.BiCleanerConstants.Companion.FILTER_BY_RULES
import cat.app.bicleaner.constant.BiCleanerConstants.Companion.MODULE_ENABLE
import cat.app.bicleaner.constant.BiCleanerConstants.Companion.REPLY_FILTER_RULES
import cat.app.bicleaner.constant.BiCleanerConstants.Companion.REQUIRED_LEVEL
import cat.app.bicleaner.constant.BiCleanerConstants.Companion.SHOW_FILTER
import cat.app.bicleaner.util.SharedPreferencesHelper

class PreferencesData(helper: SharedPreferencesHelper) {

    var moduleEnable: Boolean by helper.getDelegate(MODULE_ENABLE, true)

    var filterByLevel: Boolean by helper.getDelegate(FILTER_BY_LEVEL, false)

    var filterByRules: Boolean by helper.getDelegate(FILTER_BY_RULES, false)

    var showFilter: Boolean by helper.getDelegate(SHOW_FILTER, false)

    var requiredLevel: Int by helper.getDelegate(REQUIRED_LEVEL, 1)

    var rules: Set<String> by helper.getDelegate(REPLY_FILTER_RULES, emptySet())

}