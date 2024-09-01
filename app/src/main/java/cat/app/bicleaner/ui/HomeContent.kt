package cat.app.bicleaner.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HomeContent(viewModel: BiCleanerViewModel) {
    val moduleDetail = viewModel.moduleDetail
    val bilibiliDetail = viewModel.bilibiliDetail
    BiCleanerStatusCard(
        activated = moduleDetail.isModuleActivated,
        moduleVersion = moduleDetail.moduleVersionName
    )
    if (!(bilibiliDetail.isBilibiliInstalled||bilibiliDetail.isBilibiliHDInstalled||bilibiliDetail.isBilibiliGlobalInstalled))
        AvailableVersionNotFoundCard()
    Spacer(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    )
    Text(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        text = "已安装的Bilibili",
        textAlign = TextAlign.Start
    )
    HorizontalDivider(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        thickness = 1.dp
    )
    if (bilibiliDetail.isBilibiliInstalled)
        BilibiliCard(
            bilibiliName = bilibiliDetail.bilibiliName,
            bilibiliIcon = bilibiliDetail.bilibiliIcon,
            bilibiliVersionName = bilibiliDetail.bilibiliVersionName,
            bilibiliVersionCode = bilibiliDetail.bilibiliVersionCode
        )
    if (bilibiliDetail.isBilibiliHDInstalled)
        BilibiliCard(
            bilibiliName = bilibiliDetail.bilibiliHDName,
            bilibiliIcon = bilibiliDetail.bilibiliHDIcon,
            bilibiliVersionName = bilibiliDetail.bilibiliHDVersionName,
            bilibiliVersionCode = bilibiliDetail.bilibiliHDVersionCode
        )
    if (bilibiliDetail.isBilibiliGlobalInstalled)
        BilibiliCard(
            bilibiliName = bilibiliDetail.bilibiliGlobalName,
            bilibiliIcon = bilibiliDetail.bilibiliGlobalIcon,
            bilibiliVersionName = bilibiliDetail.bilibiliGlobalVersionName,
            bilibiliVersionCode = bilibiliDetail.bilibiliGlobalVersionCode
        )
    //BiCleanerDetailCard()
}