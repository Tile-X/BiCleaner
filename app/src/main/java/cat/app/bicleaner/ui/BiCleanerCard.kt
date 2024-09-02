package cat.app.bicleaner.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.app.bicleaner.R

@Composable
fun BiCleanerStatusCard(
    modifier: Modifier = Modifier,
    activated: Boolean,
    moduleVersion: String
) {
    Card (
        modifier = modifier
            .padding(16.dp)
            .requiredHeight(96.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxHeight(),
                imageVector = if (activated) Icons.Outlined.CheckCircle else Icons.Outlined.Info,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
            )
            Column {
                Text(
                    text = if (activated) stringResource(R.string.module_activated) else stringResource(R.string.module_not_activated),
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text =  if (activated) moduleVersion else stringResource(R.string.module_activated_tips)
                )
            }
        }
    }
}

@Composable
fun AvailableVersionNotFoundCard() {
    Card (
        modifier = Modifier
            .padding(16.dp)
            .requiredHeight(64.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row (
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight(),
                imageVector = Icons.Filled.Build,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(R.string.available_bilibili_version_not_found),
                fontSize = 18.sp,
            )
        }
    }
}

@Composable
fun BilibiliCard(
    bilibiliName: String,
    bilibiliIcon: ImageBitmap,
    bilibiliVersionName: String,
    bilibiliVersionCode: Int
) {
    Card (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .requiredHeight(72.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.card_bg_pink)),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row (
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                bitmap = bilibiliIcon,
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
            Column (
                modifier = Modifier
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = bilibiliName,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    text = stringResource(R.string.version_format, bilibiliVersionName, bilibiliVersionCode),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
fun BiCleanerDetailCard() {
    Card (
        modifier = Modifier
            .padding(16.dp)
            .requiredHeight(128.dp)
            .fillMaxWidth(),
        border = BorderStroke(1.dp, Color.DarkGray),
        colors = CardDefaults.elevatedCardColors()
    ) {
        Column (
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.total_filter_reply_count),
                    fontSize = 20.sp,
                )
                Text(
                    text = stringResource(R.string.na),
                    fontSize = 20.sp,
                    color = Color.Gray,
                    textDecoration = TextDecoration.Underline
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.total_filter_member_count),
                    fontSize = 20.sp,
                )
                Text(
                    text = stringResource(R.string.na),
                    fontSize = 20.sp,
                    color = Color.Gray,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}