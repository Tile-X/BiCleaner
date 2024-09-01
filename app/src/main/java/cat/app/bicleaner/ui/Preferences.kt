package cat.app.bicleaner.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun PreviewBiCleanerPreference() {
    //BiCleanerSwitchPreference(title = "开启模块", summary = "Off", checked = false)
    var selectedIndex by remember { mutableIntStateOf(0) }
    BiCleanerSelectionPreference(
        title = "模块过滤阈值",
        selectedIndex = selectedIndex,
        items = setOf("LV.1", "LV.2", "LV.3"),
        onSelectedChanged = { index -> selectedIndex = index }
    )
}

@Composable
fun BiCleanerPreference(
    modifier: Modifier = Modifier,
    title: String,
    summary: String,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(80.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 18.sp
            )
            Text(
                text = summary,
                color = Color.Gray
            )
        }
        Surface (
            modifier = Modifier
                .padding(16.dp),
            content = content
        )
    }
}

@Composable
fun BiCleanerSwitchPreference(
    modifier: Modifier = Modifier,
    title: String,
    summary: String,
    checked: Boolean,
    onClick: () -> Unit = {},
    onCheckedChanged: (Boolean) -> Unit = {}
) {
    BiCleanerPreference(
        modifier = modifier,
        title = title,
        summary = summary,
        onClick = onClick
        ) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChanged
        )
    }
}

@Composable
fun BiCleanerSelectionPreference(
    modifier: Modifier = Modifier,
    title: String,
    selectedIndex: Int,
    items: Set<String>,
    onClick: () -> Unit = {},
    onSelectedChanged: (Int) -> Unit = {}
) {
    BiCleanerPreference(
        modifier = modifier,
        title = title,
        summary = items.elementAt(selectedIndex),
        onClick = onClick
    ) {
        var isExpanded by remember { mutableStateOf(false) }
        Column {
            Button(
                onClick = { isExpanded = !isExpanded },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black)
            ) {
                if (isExpanded) Icon(Icons.Outlined.KeyboardArrowUp, null) else Icon(Icons.Outlined.KeyboardArrowDown, null)
            }
            DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                items.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            isExpanded = false
                            onSelectedChanged(index)
                        }
                    )
                }
            }
        }
    }
}