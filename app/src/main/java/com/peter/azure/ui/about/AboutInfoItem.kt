/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.about

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.peter.azure.R
import com.peter.azure.data.entity.Info

@Composable
fun AboutInfoItem(
    infoType: Info.Type,
    navigateToInfo: (Info.Type) -> Unit,
    modifier: Modifier
) {
    val title = when (infoType) {
        Info.Type.SERVICE -> stringResource(R.string.service_title)
        Info.Type.PRIVACY -> stringResource(R.string.privacy_title)
        Info.Type.CREDIT -> stringResource(R.string.credit_title)
    }
    val heading = title to infoType
    val description = stringResource(
        R.string.navigate_to_contract_description, heading.second.name
    )

    OutlinedButton(
        onClick = { navigateToInfo(heading.second) },
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
    ) {
        Text(
            text = heading.first,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.semantics { text = AnnotatedString("") }
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(R.drawable.ic_arrow_forward_24),
            contentDescription = description,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(24.dp)
        )
    }
}
