package cat.app.bicleaner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cat.app.bicleaner.ui.theme.BiCleanerTheme
import de.robv.android.xposed.XposedHelpers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BiCleanerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        text = if (MainActivity.isModuleActive()) "模块启用" else "模块未启用",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    companion object {
        fun isModuleActive(): Boolean {
            return false
        }
    }
}