package cat.app.bicleaner.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import cat.app.bicleaner.R
import cat.app.bicleaner.theme.BiCleanerAppTheme

@Composable
fun BiCleanerApp(viewModel: BiCleanerViewModel) {
    val selectedIndex = remember { mutableIntStateOf(0) }
    BiCleanerAppTheme {
        BiCleanerScaffold(
            enabled = viewModel.moduleDetail.isModuleActivated,
            modifier = Modifier.fillMaxSize(),
            bottomBarSelectedIndex = selectedIndex
        ) {
            when (selectedIndex.intValue) {
                0 -> HomeContent(viewModel)
                1 -> LoggingContent()
                2 -> SettingContent(viewModel)
            }
        }
    }
}

@Composable
fun BiCleanerScaffold(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    bottomBarSelectedIndex: MutableState<Int>,
    content: @Composable ColumnScope.() -> Unit
) {
    val titles = stringArrayResource(R.array.titles).toSet()
    val icons = setOf(Icons.Filled.Home, Icons.Filled.Info, Icons.Filled.Settings)
    Scaffold (
        modifier = modifier,
        topBar = { BiCleanerTopAppBar() },
        bottomBar = {
            BiCleanerNavigationBar(
                enabled = enabled,
                icons = icons,
                titles = titles,
                selectedIndex = bottomBarSelectedIndex
            )
        }
    ) {
        innerPadding -> Column (modifier = Modifier.padding(innerPadding), content = content)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiCleanerTopAppBar(
    modifier: Modifier = Modifier,
    onMenuActionClick: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(stringResource(R.string.app_name)) },
        actions = {
            IconButton(onClick = onMenuActionClick) {
                Icon(Icons.Filled.Menu, null)
            }
        }
    )
}

@Composable
fun BiCleanerNavigationBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedIndex: MutableState<Int>,
    titles: Set<String>,
    icons: Set<ImageVector>
) {
    NavigationBar (modifier = modifier) {
        titles.forEachIndexed { index, title ->
            NavigationBarItem(
                enabled = enabled,
                selected = selectedIndex.value == index,
                onClick = { selectedIndex.value = index },
                label = { Text(title) },
                icon = { Icon(icons.elementAt(index), title) }
            )
        }
    }
}