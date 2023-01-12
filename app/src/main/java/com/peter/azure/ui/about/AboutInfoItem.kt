/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.about

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peter.azure.R
import com.peter.azure.data.entity.Info

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutInfoItem(
    heading: Pair<String, Info.Type>,
    navigateToInfo: (Info.Type) -> Unit,
    modifier: Modifier
) {
    OutlinedCard(
        onClick = { navigateToInfo(heading.second) },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = heading.first,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.ic_arrow_forward_24),
                contentDescription = stringResource(R.string.icon_cd_navigate_to, heading),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
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
