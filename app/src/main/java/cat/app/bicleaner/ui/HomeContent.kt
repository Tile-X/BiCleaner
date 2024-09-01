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
import cat.app.bicleaner.data.BilibiliDetail
import cat.app.bicleaner.data.ModuleDetail

@Composable
fun HomeContent(
    moduleDetail: ModuleDetail,
    bilibiliDetail: BilibiliDetail
) {
    BiCleanerStatusCard(
        activated = moduleDetail.isModuleActivated,
        moduleVersion = moduleDetail.moduleVersionName
    )
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
    BilibiliCard(
        bilibiliName = bilibiliDetail.bilibiliName,
        bilibiliIcon = bilibiliDetail.bilibiliIcon,
        bilibiliVersionName = bilibiliDetail.bilibiliVersionName,
        bilibiliVersionCode = bilibiliDetail.bilibiliVersionCode
    )
    //BiCleanerDetailCard()
}