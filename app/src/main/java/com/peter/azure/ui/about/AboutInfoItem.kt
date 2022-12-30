package com.peter.azure.ui.about

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peter.azure.R
import com.peter.azure.data.entity.Info
import com.peter.azure.ui.theme.AzureTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutInfoItem(
    heading: Pair<String, Info.Type>,
    navigateToInfo: (String) -> Unit
) {
    OutlinedCard(
        onClick = { navigateToInfo(heading.second.name) },
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                text = heading.first,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.ic_arrow_forward_24),
                contentDescription = stringResource(R.string.icon_cd_navigate_to, heading),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun AboutInfoItemPreview() {
//    AzureTheme {
//        AboutInfoItem() {}
//    }
//}
