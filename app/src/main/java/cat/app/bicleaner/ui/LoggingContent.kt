package cat.app.bicleaner.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoggingContent() {
    BiCleanerLoggingField()
}

@Composable
fun BiCleanerLoggingField() {
    OutlinedTextField(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        readOnly = true,
        label = { Text("日志") },
        value = "日志功能暂不可用",
        onValueChange = {}
    )
}