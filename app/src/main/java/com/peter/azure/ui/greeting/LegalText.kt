/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.greeting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.azure.data.entity.Info

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LegalText(
    isCompact: Boolean,
    loadInfo: (Info.Type) -> Unit
) {
    val fontSize = if (isCompact) {
        16
    } else {
        18
    }

    FlowRow(
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        LegalGeneralText(
            text = "Please read our ",
            fontSize = fontSize
        )
        LegalLinkedText(
            text = "service terms",
            fontSize = fontSize,
            onclick = { loadInfo(Info.Type.SERVICE) }
        )
        LegalGeneralText(
            text = ", ",
            fontSize = fontSize
        )
        LegalLinkedText(
            text = "privacy policy",
            fontSize = fontSize,
            onclick = { loadInfo(Info.Type.PRIVACY) }
        )
        LegalGeneralText(
            text = " and ",
            fontSize = fontSize
        )
        LegalLinkedText(
            text = "acknowledgements",
            fontSize = fontSize,
            onclick = { loadInfo(Info.Type.ACKNOWLEDGEMENTS) }
        )
        LegalGeneralText(
            text = ".",
            fontSize = fontSize
        )
    }
}

@Composable
fun LegalLinkedText(
    text: String,
    fontSize: Int,
    onclick: () -> Unit
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary,
        fontSize = fontSize.sp,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable { onclick() }
    )
}

@Composable
fun LegalGeneralText(
    text: String,
    fontSize: Int,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = fontSize.sp,
        modifier = Modifier.semantics {
            this.text = AnnotatedString(
                "Please read our service terms, privacy policy and acknowledgements."
            )
        }
    )
}
