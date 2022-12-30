package com.peter.azure.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peter.azure.R
import com.peter.azure.data.entity.DataResult
import com.peter.azure.ui.theme.AzureTheme

@Composable
fun ErrorNotice(code: DataResult.Error.Code) {
    val reason = when (code) {
        DataResult.Error.Code.UNKNOWN -> stringResource(R.string.error_unknown)
        DataResult.Error.Code.IO -> stringResource(R.string.error_io)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(R.drawable.example_img),
            contentDescription = "dummy logo",
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(128.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = reason,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 3
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutAppItemPreview() {
    AzureTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ErrorNotice(
                DataResult.Error.Code.UNKNOWN
            )
        }
    }
}
