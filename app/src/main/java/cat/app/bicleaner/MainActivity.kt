package cat.app.bicleaner

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cat.app.bicleaner.ui.BiCleanerApp
import cat.app.bicleaner.ui.BiCleanerViewModel
import cat.app.bicleaner.util.ModuleUtils


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BiCleanerApp(BiCleanerViewModel(this))
        }
        Log.d("lookcat", "dir: " + ModuleUtils.getPreferencesDir(this))
    }
}