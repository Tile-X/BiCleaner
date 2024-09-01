package cat.app.bicleaner.data

data class PreferencesDetail (
    val moduleEnable: Boolean,
    val filterByLevel: Boolean,
    val filterByRules: Boolean,
    val showFilter: Boolean,
    val requiredLevel: Int,
    val ruleCount: Int = 0,

    val isShowAddRulesDialog: Boolean = false,
    val isShowClearRulesDialog: Boolean = false
)