package cat.app.bicleaner.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import cat.app.bicleaner.R

@Composable
fun SettingContent(viewModel: BiCleanerViewModel) {
    val detail by viewModel.preferencesDetail.collectAsState()
    var ruleText by remember { mutableStateOf("") }
    BiCleanerSwitchPreference(
        title = stringResource(R.string.module_enable),
        summary = if (detail.moduleEnable) stringResource(R.string.enable) else stringResource(R.string.disable),
        checked = detail.moduleEnable,
        onCheckedChanged = {checked -> viewModel.onModuleEnableSwitchChanged(checked)}
    )
    BiCleanerSwitchPreference(
        title = stringResource(R.string.show_filter_replies),
        summary = if (detail.showFilter) stringResource(R.string.enable) else stringResource(R.string.disable),
        checked = detail.showFilter,
        onCheckedChanged = {checked -> viewModel.onShowFilterSwitchChanged(checked)}
    )
    BiCleanerSwitchPreference(
        title = stringResource(R.string.filter_by_level),
        summary = if (detail.filterByLevel) stringResource(R.string.enable) else stringResource(R.string.disable),
        checked = detail.filterByLevel,
        onCheckedChanged = {checked -> viewModel.onFilterByLevelSwitchChanged(checked)}
    )
    BiCleanerSelectionPreference(
        title = stringResource(R.string.filter_required_level),
        selectedIndex = detail.requiredLevel,
        items = stringArrayResource(R.array.levels).toSet(),
        onSelectedChanged = {selected -> viewModel.onRequiredLevelSelectedChanged(selected)}
    )
    BiCleanerSwitchPreference(
        title = stringResource(R.string.filter_by_rules),
        summary = if (detail.filterByRules) stringResource(R.string.enable) else stringResource(R.string.disable),
        checked = detail.filterByRules,
        onCheckedChanged = {checked -> viewModel.onFilterByRulesSwitchChanged(checked)}
    )
    BiCleanerPreference(
        title = stringResource(R.string.add_reply_filter_rules),
        summary = stringResource(R.string.row_format, detail.ruleCount),
        onClick = {viewModel.onAddRulesClicked()}
    )
    BiCleanerPreference(
        title = stringResource(R.string.clear_reply_filter_rules),
        summary = "",
        onClick = {viewModel.onClearRulesClicked()}
    )
    if (detail.isShowAddRulesDialog)
        BiCleanerTextFieldDialog(
            title = stringResource(R.string.add_reply_filter_rules),
            textValue = ruleText,
            onTextValueChanged = {text -> ruleText = text},
            onConfirmButtonClick = { viewModel.onAddRulesDialogConfirmClicked(ruleText) },
            onDismissButtonClick = { viewModel.onAddRulesDialogDismissClicked() }
        )
    if (detail.isShowClearRulesDialog)
        BiCleanerAlertDialog(
            title = stringResource(R.string.clear_reply_filter_rules),
            text = stringResource(R.string.clear_all_reply_rule_ask),
            onConfirmButtonClick = { viewModel.onClearRulesDialogConfirmClicked() },
            onDismissButtonClick = { viewModel.onClearRulesDialogDismissClicked() }
        )
}