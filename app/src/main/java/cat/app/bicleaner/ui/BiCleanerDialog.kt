package cat.app.bicleaner.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable

@Composable
fun BiCleanerTextFieldDialog(
    title: String,
    textValue: String,
    onTextValueChanged: (String) -> Unit = {},
    onConfirmButtonClick: () -> Unit = {},
    onDismissButtonClick: () -> Unit = {}
) {
    AlertDialog(
        title = { Text(title) },
        text = {
            TextField(
                value = textValue,
                maxLines = 10,
                onValueChange = onTextValueChanged,
            )
        },
        confirmButton = { TextButton(onClick = onConfirmButtonClick) { Text("确定") } },
        dismissButton = { TextButton(onClick = onDismissButtonClick) { Text("取消") } },
        onDismissRequest = { }
    )
}

@Composable
fun BiCleanerAlertDialog(
    title: String,
    text: String,
    onConfirmButtonClick: () -> Unit = {},
    onDismissButtonClick: () -> Unit = {}
) {
    AlertDialog(
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = { TextButton(onClick = onConfirmButtonClick) { Text("确定") } },
        dismissButton = { TextButton(onClick = onDismissButtonClick) { Text("取消") } },
        onDismissRequest = { },
    )
}