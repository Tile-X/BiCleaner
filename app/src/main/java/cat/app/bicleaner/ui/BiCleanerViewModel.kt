package cat.app.bicleaner.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import cat.app.bicleaner.constant.BiCleanerConstants.Companion.PREFERENCES_NAME
import cat.app.bicleaner.constant.BiCleanerConstants.Companion.RULES_FILENAME
import cat.app.bicleaner.data.BilibiliDetail
import cat.app.bicleaner.data.ModuleDetail
import cat.app.bicleaner.data.PreferencesData
import cat.app.bicleaner.data.PreferencesDetail
import cat.app.bicleaner.util.FileHelper
import cat.app.bicleaner.util.ModuleUtils
import cat.app.bicleaner.util.SharedPreferencesHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BiCleanerViewModel(context: Context) : ViewModel() {

    val bilibiliDetail = BilibiliDetail(context.packageManager)

    val moduleDetail = ModuleDetail()

    @SuppressLint("WorldReadableFiles")
    private val preferencesData = PreferencesData(SharedPreferencesHelper(context.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_WORLD_READABLE)))

    private val rulesFile = FileHelper(ModuleUtils.getPreferencesDir(context), RULES_FILENAME)

    private val _preferencesDetail = MutableStateFlow(
        PreferencesDetail(
            moduleEnable = preferencesData.moduleEnable,
            filterByLevel = preferencesData.filterByLevel,
            filterByRules = preferencesData.filterByRules,
            showFilter = preferencesData.showFilter,
            requiredLevel = preferencesData.requiredLevel - 1,
            ruleCount = rulesFile.reads().size
        )
    )

    val preferencesDetail = _preferencesDetail.asStateFlow()

    fun onModuleEnableSwitchChanged(checked: Boolean) {
        preferencesData.moduleEnable = checked
        _preferencesDetail.update { detail -> detail.copy(moduleEnable = checked) }
    }

    fun onShowFilterSwitchChanged(checked: Boolean) {
        preferencesData.showFilter = checked
        _preferencesDetail.update { detail -> detail.copy(showFilter = checked) }
    }

    fun onFilterByLevelSwitchChanged(checked: Boolean) {
        preferencesData.filterByLevel = checked
        _preferencesDetail.update { detail -> detail.copy(filterByLevel = checked) }
    }

    fun onFilterByRulesSwitchChanged(checked: Boolean) {
        preferencesData.filterByRules = checked
        _preferencesDetail.update { detail -> detail.copy(filterByRules = checked) }
    }

    fun onRequiredLevelSelectedChanged(selected: Int) {
        preferencesData.requiredLevel = selected + 1
        _preferencesDetail.update { detail -> detail.copy(requiredLevel = selected) }
    }

    fun onAddRulesDialogDismissClicked() {
        _preferencesDetail.update { detail -> detail.copy(isShowAddRulesDialog = false) }
    }

    fun onClearRulesDialogDismissClicked() {
        _preferencesDetail.update { detail -> detail.copy(isShowClearRulesDialog = false) }
    }

    fun onAddRulesDialogConfirmClicked(rulesStr: String) {
        Log.d("lookcat", "rules: $rulesStr")
        val rules = rulesStr.split(System.lineSeparator())
        val cnt = rulesFile.lines(rules)
        _preferencesDetail.update { detail -> detail.copy(ruleCount = detail.ruleCount + cnt, isShowAddRulesDialog = false) }
    }

    fun onClearRulesDialogConfirmClicked() {
        rulesFile.clear()
        _preferencesDetail.update { detail -> detail.copy(ruleCount = 0, isShowClearRulesDialog = false) }
    }

    fun onAddRulesClicked() {
        _preferencesDetail.update { detail -> detail.copy(isShowAddRulesDialog = true) }
    }

    fun onClearRulesClicked() {
        _preferencesDetail.update { detail -> detail.copy(isShowClearRulesDialog = true) }
    }

}