package cat.app.bicleaner.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun SettingContent(viewModel: BiCleanerViewModel) {
    val detail by viewModel.preferencesDetail.collectAsState()
    var ruleText by remember { mutableStateOf("") }
    BiCleanerSwitchPreference(
        title = "启用模块",
        summary = if (detail.moduleEnable) "启用" else "关闭",
        checked = detail.moduleEnable,
        onCheckedChanged = {checked -> viewModel.onModuleEnableSwitchChanged(checked)}
    )
    BiCleanerSwitchPreference(
        title = "保留过滤痕迹",
        summary = if (detail.showFilter) "启用" else "关闭",
        checked = detail.showFilter,
        onCheckedChanged = {checked -> viewModel.onShowFilterSwitchChanged(checked)}
    )
    BiCleanerSwitchPreference(
        title = "根据等级过滤",
        summary = if (detail.filterByLevel) "启用" else "关闭",
        checked = detail.filterByLevel,
        onCheckedChanged = {checked -> viewModel.onFilterByLevelSwitchChanged(checked)}
    )
    BiCleanerSelectionPreference(
        title = "评论过滤阈值",
        selectedIndex = detail.requiredLevel,
        items = setOf("LV.1", "LV.2", "LV.3", "LV.4", "LV.5", "LV.6"),
        onSelectedChanged = {selected -> viewModel.onRequiredLevelSelectedChanged(selected)}
    )
    BiCleanerSwitchPreference(
        title = "根据内容过滤",
        summary = if (detail.filterByRules) "启用" else "关闭",
        checked = detail.filterByRules,
        onCheckedChanged = {checked -> viewModel.onFilterByRulesSwitchChanged(checked)}
    )
    BiCleanerPreference(
        title = "添加过滤规则",
        summary = "${detail.ruleCount}条",
        onClick = {viewModel.onAddRulesClicked()}
    )
    BiCleanerPreference(
        title = "清除所有规则",
        summary = "",
        onClick = {viewModel.onClearRulesClicked()}
    )
    if (detail.isShowAddRulesDialog)
        BiCleanerTextFieldDialog(
            title = "添加过滤规则",
            textValue = ruleText,
            onTextValueChanged = {text -> ruleText = text},
            onConfirmButtonClick = { viewModel.onAddRulesDialogConfirmClicked(ruleText) },
            onDismissButtonClick = { viewModel.onAddRulesDialogDismissClicked() }
        )
    if (detail.isShowClearRulesDialog)
        BiCleanerAlertDialog(
            title = "清除过滤规则",
            text = "您确定要删除所有过滤规则吗?",
            onConfirmButtonClick = { viewModel.onClearRulesDialogConfirmClicked() },
            onDismissButtonClick = { viewModel.onClearRulesDialogDismissClicked() }
        )
}